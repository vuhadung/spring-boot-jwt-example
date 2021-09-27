package com.fortna.hackathon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fortna.hackathon.dto.AppResponse;
import com.fortna.hackathon.entity.Language;
import com.fortna.hackathon.service.LanguageService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @GetMapping(value = "/language", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllLanguages() {
        List<Language> languages = languageService.getAllLanguages();
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, languages));
    }

}
