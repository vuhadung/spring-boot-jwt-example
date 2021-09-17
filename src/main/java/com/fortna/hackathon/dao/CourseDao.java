package com.fortna.hackathon.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fortna.hackathon.entity.Course;

@Repository
public interface CourseDao extends CrudRepository<Course, Long> {

    Course findById(long id);

    List<Course> findAll();

}
