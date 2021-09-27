package com.fortna.hackathon.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fortna.hackathon.dao.CourseDao;
import com.fortna.hackathon.entity.Course;
import com.fortna.hackathon.service.CourseService;

@Service(value = "courseService")
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDAO;

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public List<Course> getCoursesByIsBackup(boolean isBackup) {
        return courseDAO.findByIsBackup(isBackup);
    }

    @Override
    public boolean canSubmitForCourse(String courseId) {
        Optional<Course> course = courseDAO.findById(Long.valueOf(courseId));
        if (!course.isPresent())
            return false;
        if (new Date().after(course.get().getDeadline()))
            return false;
        return true;
    }

}
