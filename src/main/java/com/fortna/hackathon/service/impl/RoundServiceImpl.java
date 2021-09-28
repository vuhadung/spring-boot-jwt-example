package com.fortna.hackathon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fortna.hackathon.dao.RoundDao;
import com.fortna.hackathon.dto.CreateRoundDto;
import com.fortna.hackathon.entity.Round;
import com.fortna.hackathon.service.RoundService;

@Service(value = "roundService")
@Transactional
public class RoundServiceImpl implements RoundService {

    @Autowired
    private RoundDao roundDao;

    @Override
    public void createRound(CreateRoundDto roundDto) {
        Round round = new Round();
        round.setName(roundDto.getRoundName());
        round.setStartDate(new Date(roundDto.getStartDate() * 1000));
        round.setEndDate(new Date(roundDto.getEndDate() * 1000));
        roundDao.save(round);
    }

    @Override
    public List<Round> getActiveRounds() {
        return roundDao.findActiveRounds();
    }

    @Override
    public List<Round> getAllRounds() {
        List<Round> result = new ArrayList<>();
        roundDao.findAll().iterator().forEachRemaining(result::add);
        return result;
    }

}
