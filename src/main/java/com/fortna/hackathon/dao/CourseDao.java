package com.fortna.hackathon.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fortna.hackathon.entity.Course;

@Repository
public interface CourseDao extends CrudRepository<Course, Long> {

    Course findById(long id);

    List<Course> findAll();
    
    @Query("from Course c where c.backup is :status")
    List<Course> findByIsBackup(@Param("status") boolean status);

}
