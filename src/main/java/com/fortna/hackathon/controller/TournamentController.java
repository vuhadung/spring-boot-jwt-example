package com.fortna.hackathon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fortna.hackathon.dto.AppResponse;
import com.fortna.hackathon.service.TournamentService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping(value = "/tournament", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTournament() {
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, tournamentService.getTournament()));
    }

}
