package com.fortna.hackathon.service;

import java.util.List;

import com.fortna.hackathon.dto.CreateRoundDto;
import com.fortna.hackathon.entity.Round;

public interface RoundService {

    void createRound(CreateRoundDto roundDto);

    List<Round> getActiveRounds();

    List<Round> getAllRounds();

}
