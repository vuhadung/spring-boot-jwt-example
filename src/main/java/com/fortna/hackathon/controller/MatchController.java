package com.fortna.hackathon.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fortna.hackathon.dto.AppResponse;
import com.fortna.hackathon.dto.CreateMatchDto;
import com.fortna.hackathon.service.MatchService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/match")
public class MatchController {

    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private MatchService matchService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/execute/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/publish-result", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> publishMatches(@RequestBody List<Long> matchIds) {
        if (matchIds != null && !matchIds.isEmpty()) {
            matchService.publishMatchesResult(matchIds);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new AppResponse(null, "Published matches' result successfully!"));
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> downloadMatch(@PathVariable("id") final Long id,
            @RequestParam(name = "turn", required = true) String turn) {
        if (turn == null || turn.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse(null, "Invalid params!"));
        }
        boolean isAwayMatch;
        if ("0".equals(turn)) {
            isAwayMatch = false;
        } else if ("1".equals(turn)) {
            isAwayMatch = true;
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse(null, "Invalid params!"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!matchService.canDownloadMatchResult(authentication.getName(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse(null, "Result not found!"));
        }

        String filePath = matchService.getPathToMatchResult(id, isAwayMatch);
        if (filePath == null || filePath.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new AppResponse(null, "File not found!"));
        }

        // return file
        try {
            File file = new File(filePath);

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.txt");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok().headers(header).contentLength(file.length())
                    .contentType(MediaType.parseMediaType("text/plain")).body(resource);
        } catch (IOException e) {
            logger.error("Error while downloading result file: {}", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AppResponse("Error while reading file!", null));

    }

}
