package com.fortna.hackathon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fortna.hackathon.dao.LanguageDao;
import com.fortna.hackathon.entity.Language;
import com.fortna.hackathon.service.LanguageService;

@Service(value = "languageService")
@Transactional
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private LanguageDao languageDAO;

    @Override
    public List<Language> getAllLanguages() {
        return languageDAO.findAllByOrderByIdAsc();
    }

}
