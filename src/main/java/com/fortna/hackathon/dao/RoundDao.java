package com.fortna.hackathon.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fortna.hackathon.entity.Round;

public interface RoundDao extends CrudRepository<Round, Long> {
    
    @Query("from Round r where r.startDate <= current_timestamp() and r.endDate >= current_timestamp()")
    List<Round> findActiveRounds();

}
