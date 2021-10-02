package com.fortna.hackathon.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fortna.hackathon.entity.Submission;

@Repository
public interface SubmissionDao extends CrudRepository<Submission, Long> {

    Submission findById(long id);

    @Query("from Submission s inner join fetch s.user where s.user.id = :userId")
    Submission findByUserId(@Param("userId") long userId);
    
    List<Submission> findAllByOrderByIdAsc();

}
