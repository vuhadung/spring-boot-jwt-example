package com.fortna.hackathon.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fortna.hackathon.dto.AppResponse;
import com.fortna.hackathon.dto.CreateRoundDto;
import com.fortna.hackathon.entity.Round;
import com.fortna.hackathon.service.RoundService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/round")
public class RoundController {

    @Autowired
    private RoundService roundService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRound(@Valid @RequestBody CreateRoundDto roundDto) {
        if (roundDto.getStartDate() == null || roundDto.getEndDate() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse("Invalid request body!", null));
        roundService.createRound(roundDto);
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, "Create round successfully!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllRounds() {
        List<Round> rounds = roundService.getAllRounds();
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, rounds));
    }

    @PreAuthorize("permitAll()")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllActiveRounds() {
        List<Round> rounds = roundService.getActiveRounds();
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, rounds));
    }

}
