package com.fortna.hackathon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fortna.hackathon.dao.RoundDao;
import com.fortna.hackathon.dto.TournamentDto;
import com.fortna.hackathon.dto.TournamentDto.MatchDto;
import com.fortna.hackathon.entity.Match;
import com.fortna.hackathon.entity.Round;
import com.fortna.hackathon.service.TournamentService;

@Service(value = "tournamentService")
@Transactional
public class TournamentServiceImpl implements TournamentService {

    private static final Logger logger = LoggerFactory.getLogger(TournamentServiceImpl.class);

    @Autowired
    private RoundDao roundDao;

    @Override
    public TournamentDto getTournament() {
        List<Round> rounds = roundDao.findAllByOrderByIdAsc();
        if (rounds == null || rounds.isEmpty()) {
            logger.error("No round found!");
            return null;
        }

        TournamentDto tournament = new TournamentDto();
        List<MatchDto> teams = new ArrayList<>();
        List<List<String>> results = new ArrayList<>();

        for (Round r : rounds) {
            List<String> resultOfRound = new ArrayList<>();
            List<Match> matches = r.getMatches();
            logger.info("Found {} matches for round {}", matches.size(), r.getName());
            for (Match m : matches) {
                MatchDto obj = new MatchDto();
                obj.setId(m.getId());
                obj.setFirstPlayer(m.getPlayer0().getDisplayName());
                obj.setSecondPlayer(m.getPlayer1().getDisplayName());
                teams.add(obj);
                String winner = m.getFinalWinner() != null ? m.getFinalWinner().getDisplayName() : "";
                resultOfRound.add(winner);
                logger.info("Match {} between {} and {}. Final winner is {}", obj.getId(), obj.getFirstPlayer(),
                        obj.getSecondPlayer(), winner != "" ? winner : "N/A");
            }
            results.add(resultOfRound);
        }

        tournament.setTeams(teams);
        tournament.setResults(results);
        return tournament;
    }

}
