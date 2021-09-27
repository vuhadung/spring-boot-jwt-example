package com.fortna.hackathon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fortna.hackathon.dto.AppResponse;
import com.fortna.hackathon.dto.CreateMatchDto;
import com.fortna.hackathon.service.MatchService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/execute/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> executeMatch(@PathVariable final Long id) {
        matchService.verifyMatch(id);
        matchService.executeMatch(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new AppResponse(null, "Send request to run game successfully!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMatch(@RequestBody CreateMatchDto createMatchDto) {
        if (createMatchDto.getFirstPlayerId() == null || createMatchDto.getSecondPlayerId() == null
                || createMatchDto.getMainCourseId() == null || createMatchDto.getBackupCourseId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse("Invalid body request!", null));
        if (!matchService.createMatch(createMatchDto))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse("Invalid body request!", null));
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, "Create match successfully!"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllMatches() {
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, matchService.getAllMatchesForAdmin()));
    }

}
