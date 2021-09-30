package com.fortna.hackathon.service.impl;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortna.hackathon.config.FileStorageConfiguration;
import com.fortna.hackathon.dao.MatchDao;
import com.fortna.hackathon.dao.RoundDao;
import com.fortna.hackathon.dao.SubmissionDao;
import com.fortna.hackathon.dao.UserDao;
import com.fortna.hackathon.dto.CreateMatchDto;
import com.fortna.hackathon.dto.GameLog;
import com.fortna.hackathon.dto.GameLog.PlayerLog;
import com.fortna.hackathon.dto.MatchMgmtDto;
import com.fortna.hackathon.entity.Course;
import com.fortna.hackathon.entity.Match;
import com.fortna.hackathon.entity.Round;
import com.fortna.hackathon.entity.Submission;
import com.fortna.hackathon.entity.User;
import com.fortna.hackathon.exception.RunGameException;
import com.fortna.hackathon.service.MatchService;
import com.fortna.hackathon.utils.GameConstant;

@Service(value = "matchService")
@Transactional
public class MatchServiceImpl implements MatchService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private static final String DRAW_RESULT = "N/A";

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoundDao roundDao;

    @Autowired
    private MatchDao matchDao;

    @Autowired
    private SubmissionDao submissionDao;

    @Autowired
    private FileStorageConfiguration fileStorageConfig;

    @Async("asyncExecutor")
    public void executeMatch(long id) {
        executeMatchWithCourse(id, true, false);
    }

    /*---------------------------- MATCH EXECUTION -----------------------------*/

    private void executeMatchWithCourse(long id, boolean isMainCourse, boolean forceStop) {
        try {
            String awayWinner = runGameAndFindWinner(id, isMainCourse, true);
            String homeWinner = runGameAndFindWinner(id, isMainCourse, false);
            boolean isDraw = false;
            if (DRAW_RESULT.equals(awayWinner) || DRAW_RESULT.equals(homeWinner)) {
                if (awayWinner.equals(homeWinner)) {
                    isDraw = true;
                }
            } else {
                if (!awayWinner.equals(homeWinner)) {
                    isDraw = true;
                }
            }
            if (!isDraw) {
                // update final winner
                updateFinalWinner(id, awayWinner, homeWinner);
            } else {
                if (!forceStop) {
                    logger.info("Game {} is draw with the main course. Run the backup course!", id);
                    executeMatchWithCourse(id, false, true);
                } else {
                    logger.info("Game {} is draw with the backup course. Admin should handle game manually!", id);
                    Match match = matchDao.findById(id);
                    match.setErrorMessage("Game is draw with the backup course. Admin should handle game manually!");
                    matchDao.save(match);
                }
            }
        } catch (Exception e) {
            logger.error("Execute match id = {} with error {}", id, e.getMessage());
            Match match = matchDao.findById(id);
            match.setErrorMessage(e.getMessage());
            matchDao.save(match);
        }
        return;
    }

    private String runGameAndFindWinner(long id, boolean isMainCourse, boolean isAwayMatch) throws Exception {
        Match match = matchDao.findById(id);

        if (match.getPlayer0() != null && match.getPlayer1() != null) {
            Course course = null;
            User player0 = null;
            User player1 = null;

            Round round = match.getRound();
            if (isMainCourse) {
                course = round.getCourse();
            } else {
                course = round.getBackupCourse();
                if (course == null) {
                    throw new Exception("Backup course is not found");
                }
            }

            if (isAwayMatch) {
                player0 = match.getPlayer0();
                player1 = match.getPlayer1();
            } else {
                player0 = match.getPlayer1();
                player1 = match.getPlayer0();
            }

            Submission filePlayer0 = submissionDao.findByUserId(player0.getId());
            Submission filePlayer1 = submissionDao.findByUserId(player1.getId());

            // prepare command arguments
            String fullPathToOfficialDir = Paths.get(this.fileStorageConfig.getOfficialDir()).toAbsolutePath()
                    .normalize().toString();
            String fullPathToCourseDir = Paths.get(this.fileStorageConfig.getCourseDir()).toAbsolutePath().normalize()
                    .toString();
            String fullPathToPlayerDir = Paths.get(this.fileStorageConfig.getPlayerDir()).toAbsolutePath().normalize()
                    .toString();
            String fullPathToResultDir = Paths.get(this.fileStorageConfig.getResultDir()).toAbsolutePath().normalize()
                    .toString();
            // need to create directory before create result file
            Files.createDirectories(Paths.get(this.fileStorageConfig.getResultDir()).toAbsolutePath().normalize());

            String pathToOfficial = new StringBuilder().append(fullPathToOfficialDir).append(File.separator)
                    .append(GameConstant.GAME_OFFICIAL_NAME).toString();
            String pathToCourse = new StringBuilder().append(fullPathToCourseDir).append(File.separator)
                    .append("course_").append(course.getId()).append(GameConstant.COURSE_FILE_EXTENSION).toString();

            String pathToPlayer0 = null;
            if (filePlayer0.getLanguage().getName().toLowerCase().contains("java")) {
                pathToPlayer0 = new StringBuilder().append(fullPathToPlayerDir).append(File.separator)
                        .append(player0.getUsername()).append(File.separator).append("runme.sh").toString();
            } else {
                pathToPlayer0 = new StringBuilder().append(fullPathToPlayerDir).append(File.separator)
                        .append(player0.getUsername()).append(File.separator).append("runme").toString();
            }
            String nameOfPlayer0 = player0.getUsername();

            String pathToPlayer1 = null;
            if (filePlayer1.getLanguage().getName().toLowerCase().contains("java")) {
                pathToPlayer1 = new StringBuilder().append(fullPathToPlayerDir).append(File.separator)
                        .append(player1.getUsername()).append(File.separator).append("runme.sh").toString();
            } else {
                pathToPlayer1 = new StringBuilder().append(fullPathToPlayerDir).append(File.separator)
                        .append(player1.getUsername()).append(File.separator).append("runme").toString();
            }
            String nameOfPlayer1 = player1.getUsername();

            String pathToResultFile = null;
            if (isAwayMatch) {
                pathToResultFile = new StringBuilder().append(fullPathToResultDir).append(File.separator)
                        .append("match_").append(id).append("_away.txt").toString();
            } else {
                pathToResultFile = new StringBuilder().append(fullPathToResultDir).append(File.separator)
                        .append("match_").append(id).append("_home.txt").toString();
            }

            // run game
            ProcessBuilder processBuilder = null;
            Process process = null;

            logger.info("Running game {} between {} and {}", id, nameOfPlayer0, nameOfPlayer1);
            logger.info("Command is: {} {} {} {} {} {}", pathToOfficial, pathToCourse, pathToPlayer0, nameOfPlayer0,
                    pathToPlayer1, nameOfPlayer1);
            processBuilder = new ProcessBuilder(pathToOfficial, pathToCourse, pathToPlayer0, nameOfPlayer0,
                    pathToPlayer1, nameOfPlayer1);
            processBuilder.redirectOutput(Redirect.to(new File(pathToResultFile)));
            process = processBuilder.start();
            process.waitFor();

            // find winner and update to DB
            return findWinnerFromResultFile(id, isAwayMatch, pathToResultFile);
        } else {
            if (match.getPlayer0() == null) {
                return match.getPlayer1().getUsername();
            } else {
                return match.getPlayer0().getUsername();
            }
        }
    }

    private void updateFinalWinner(long id, String awayWinner, String homeWinner) {
        String finalWinner = null;
        if (DRAW_RESULT.equals(awayWinner)) {
            finalWinner = homeWinner;
        } else if (DRAW_RESULT.equals(homeWinner)) {
            finalWinner = awayWinner;
        } else {
            finalWinner = awayWinner;
        }
        User user = userDao.findByUsername(finalWinner);
        Match match = matchDao.findById(id);
        match.setFinalWinner(user);
        match.setResultPublished(false);
        match.setErrorMessage(null);
        matchDao.save(match);
        logger.info("Game {} runs successfully! Winner is {}", id, finalWinner);
    }

    private String findWinnerFromResultFile(long id, boolean isAwayMatch, String pathToResultFile) throws Exception {
        Match match = matchDao.findById(id);
        User winner = null;
        String winnerName = null;
        boolean isDraw;
        // parse log file
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GameLog gameLog = objectMapper.readValue(new File(pathToResultFile), GameLog.class);

        // The player who crosses the finish line first will be the winner
        if (gameLog.getTime0() == gameLog.getTime1()) {
            isDraw = true;
        } else {
            winnerName = gameLog.getTime0() < gameLog.getTime1() ? gameLog.getName0() : gameLog.getName1();
            isDraw = false;
        }

        if (isDraw) {
            PlayerLog player0Log = gameLog.getLog0().get(gameLog.getLog0().size() - 1);
            PlayerLog player1Log = gameLog.getLog1().get(gameLog.getLog1().size() - 1);
            // If both players fail to cross the finish line within the time limit, the
            // player with a higher y coordinate (i.e: the player who runs further) will be
            // the winner
            if (player0Log.getBefore().getY() != player1Log.getBefore().getY()) {
                winnerName = player0Log.getBefore().getY() > player1Log.getBefore().getY() ? gameLog.getName0()
                        : gameLog.getName1();
                isDraw = false;
            } else {
                // If both players have the same y coordinate, the player who reaches this y
                // coordinate first (i.e: the player who runs faster) will be the winner.
                if (player0Log.getStep() != player1Log.getStep()) {
                    winnerName = player0Log.getStep() < player1Log.getStep() ? gameLog.getName0() : gameLog.getName1();
                    isDraw = false;
                } else {
                    // If both players reach the same y coordinate on the same round, the match is
                    // considered draw. The game will then be repeated using a different map.
                    winnerName = DRAW_RESULT;
                }
            }
        }

        // update to DB
        if (!DRAW_RESULT.equals(winnerName)) {
            winner = userDao.findByUsername(winnerName);
            if (isAwayMatch) {
                match.setAwayMatchWinner(winner);
            } else {
                match.setHomeMatchWinner(winner);
            }
        }
        if (isAwayMatch) {
            match.setPathToAwayMatchResult(pathToResultFile);
        } else {
            match.setPathToHomeMatchResult(pathToResultFile);
        }
        matchDao.save(match);
        return winnerName;
    }

    public void verifyMatch(long id) {
        Match match = matchDao.findById(id);
        if (match == null) {
            throw new RunGameException("Match id = " + id + " not found");
        }

        Round round = match.getRound();
        if (round == null) {
            throw new RunGameException("Match id = " + id + " does not belongs to any round");
        }

        Course course = round.getCourse();
        if (course == null) {
            throw new RunGameException("Course for match id = " + id + " not found");
        }

        User player0 = match.getPlayer0();
        User player1 = match.getPlayer1();
        if (player0 != null && player1 != null) {
            Submission filePlayer0 = submissionDao.findByUserId(player0.getId());
            Submission filePlayer1 = submissionDao.findByUserId(player1.getId());

            if (filePlayer0 == null && filePlayer1 == null) {
                throw new RunGameException("No submission found for both players!");
            }

            String pathToOfficial = new StringBuilder().append(fileStorageConfig.getOfficialDir())
                    .append(File.separator).append(GameConstant.GAME_OFFICIAL_NAME).toString();
            String pathToCourse = new StringBuilder().append(fileStorageConfig.getCourseDir()).append(File.separator)
                    .append("course_").append(course.getId()).append(GameConstant.COURSE_FILE_EXTENSION).toString();

            if (!new File(pathToOfficial).exists()) {
                throw new RunGameException("Official file is not found");
            }

            if (!new File(pathToCourse).exists()) {
                throw new RunGameException("Course " + course.getName() + " file is not found");
            }
        }
    }

    /*---------------------------- MATCH MANAGEMENT -----------------------------*/

    @Override
    public boolean createMatch(CreateMatchDto matchDto) {
        Optional<Round> round = roundDao.findById(matchDto.getRoundId());
        Optional<User> player0 = userDao.findById(matchDto.getFirstPlayerId());
        Optional<User> player1 = userDao.findById(matchDto.getSecondPlayerId());

        if (!round.isPresent() || (!player0.isPresent() && !player1.isPresent())) {
            logger.error("Round or player not found!");
            return false;
        }

        Match match = new Match();
        match.setRound(round.get());
        if (player0.isPresent())
            match.setPlayer0(player0.get());
        if (player1.isPresent())
            match.setPlayer1(player1.get());
        match.setCreatedDate(new Date());
        match.setResultPublished(false);
        match.setUpdatedDate(new Date());
        matchDao.save(match);

        return true;
    }

    @Override
    public List<MatchMgmtDto> getAllMatchesForAdmin() {
        List<Match> matches = matchDao.findAllByOrderByIdAsc();
        List<MatchMgmtDto> results = matches.stream().map(m -> {
            MatchMgmtDto dto = new MatchMgmtDto();
            dto.setId(m.getId());
            dto.setFirstPlayer(m.getPlayer0().getDisplayName());
            dto.setSecondPlayer(m.getPlayer1().getDisplayName());
            dto.setAwayMatchWinner(m.getAwayMatchWinner() != null ? m.getAwayMatchWinner().getDisplayName() : "");
            dto.setHomeMatchWinner(m.getHomeMatchWinner() != null ? m.getHomeMatchWinner().getDisplayName() : "");
            dto.setFinalWinner(m.getFinalWinner() != null ? m.getFinalWinner().getDisplayName() : "");
            dto.setErrorMessage(m.getErrorMessage() != null ? m.getErrorMessage() : "");
            dto.setResultPublished(m.isResultPublished());
            return dto;
        }).collect(Collectors.toList());
        return results;
    }

    @Override
    public void publishMatchesResult(List<Long> matchIds) {
        for (Long id : matchIds) {
            Optional<Match> m = matchDao.findById(id);
            if (m.isPresent()) {
                Match match = m.get();
                match.setResultPublished(true);
                matchDao.save(match);
            }
        }
    }

    @Override
    public boolean canDownloadMatchResult(String username, Long matchId) {
        Optional<Match> m = matchDao.findById(matchId);
        if (!m.isPresent()) {
            return false;
        }
        // Admin can download any match result
        User user = userDao.findByUsername(username);
        if (user.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getRole().getName()))) {
            return true;
        }
        Match match = m.get();
        return match.isResultPublished();
    }

    @Override
    public String getPathToMatchResult(long matchId, boolean isAwayMatch) {
        Match match = matchDao.findById(matchId);
        if (isAwayMatch) {
            return match.getPathToAwayMatchResult() != null ? match.getPathToAwayMatchResult() : new String();
        } else {
            return match.getPathToHomeMatchResult() != null ? match.getPathToHomeMatchResult() : new String();
        }
    }
}
