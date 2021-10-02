package com.fortna.hackathon.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fortna.hackathon.entity.Language;

@Repository
public interface LanguageDao extends CrudRepository<Language, Long> {

    Language findById(long id);

    List<Language> findAllByOrderByIdAsc();

}
