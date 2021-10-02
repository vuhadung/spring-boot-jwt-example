package com.fortna.hackathon.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fortna.hackathon.dao.RoundDao;
import com.fortna.hackathon.dto.TournamentDto;
import com.fortna.hackathon.dto.TournamentDto.MatchDto;
import com.fortna.hackathon.dto.TournamentDto.ScoreDto;
import com.fortna.hackathon.entity.Match;
import com.fortna.hackathon.entity.Round;
import com.fortna.hackathon.service.TournamentService;
import com.fortna.hackathon.utils.ImageProcessing;

@Service(value = "tournamentService")
@Transactional
public class TournamentServiceImpl implements TournamentService {

    private static final Logger logger = LoggerFactory.getLogger(TournamentServiceImpl.class);

    @Autowired
    private RoundDao roundDao;

    @Override
    public TournamentDto getTournament() {
        List<Round> rounds = roundDao.findAllByOrderByStartDateAsc();
        if (rounds == null || rounds.isEmpty()) {
            logger.error("No round found!");
            return null;
        }

        TournamentDto tournament = new TournamentDto();
        List<MatchDto> teams = new ArrayList<>();
        List<List<ScoreDto>> results = new ArrayList<>();

        for (Round r : rounds) {
            List<ScoreDto> resultOfRound = new ArrayList<>();
            List<Match> matches = r.getMatches();
            matches.sort(Comparator.comparing(Match::getId));
            logger.info("Found {} matches for round {}", matches.size(), r.getName());
            for (Match m : matches) {
                MatchDto obj = new MatchDto();
                obj.setRoundId(m.getRound().getId());
                obj.setId(m.getId());

                if (m.getPlayer0() != null) {
                    obj.setFirstPlayerId(m.getPlayer0().getId());
                    obj.setFirstPlayer(m.getPlayer0().getDisplayName());
                    if (m.getPlayer0().getAvatar() != null)
                        obj.setFirstPlayerAvatar(ImageProcessing.compressAvatar(m.getPlayer0().getAvatar()));
                }

                if (m.getPlayer1() != null) {
                    obj.setSecondPlayerId(m.getPlayer1().getId());
                    obj.setSecondPlayer(m.getPlayer1().getDisplayName());
                    if (m.getPlayer1().getAvatar() != null)
                        obj.setSecondPlayerAvatar(ImageProcessing.compressAvatar(m.getPlayer1().getAvatar()));
                }
                teams.add(obj);

                if (m.isResultPublished() && m.getFinalWinner() != null) {
                    ScoreDto scoreDto = new ScoreDto();
                    if (m.getFinalWinner().getId() == m.getPlayer0().getId()) {
                        scoreDto.setScore(new ArrayList<>(Arrays.asList(2, 0)));
                    } else {
                        scoreDto.setScore(new ArrayList<>(Arrays.asList(0, 2)));
                    }
                    resultOfRound.add(scoreDto);
                    logger.info("Match {} between {} and {}. Final winner is {}", obj.getId(), obj.getFirstPlayer(),
                            obj.getSecondPlayer(), m.getFinalWinner().getDisplayName());
                } else {
                    ScoreDto scoreDto = new ScoreDto();
                    scoreDto.setScore(new ArrayList<>(Arrays.asList(0, 0)));
                    resultOfRound.add(scoreDto);
                    logger.info("Match {} between {} and {}. Final winner is not published yet or the game is draw!",
                            obj.getId(), obj.getFirstPlayer(), obj.getSecondPlayer());
                }
            }
            results.add(resultOfRound);
        }

        tournament.setTeams(teams);
        tournament.setResults(results);
        return tournament;
    }

}
