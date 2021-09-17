package com.fortna.hackathon.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fortna.hackathon.entity.Match;

@Repository
public interface MatchDao extends CrudRepository<Match, Long> {

    Match findById(long id);

    List<Match> findAll();

}
