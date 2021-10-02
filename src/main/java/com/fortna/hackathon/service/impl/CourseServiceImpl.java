package com.fortna.hackathon.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fortna.hackathon.dao.CourseDao;
import com.fortna.hackathon.dao.RoundDao;
import com.fortna.hackathon.entity.Course;
import com.fortna.hackathon.entity.Round;
import com.fortna.hackathon.service.CourseService;

@Service(value = "courseService")
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDAO;
    
    @Autowired
    private RoundDao roundDAO;

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAllByOrderByIdAsc();
    }

    @Override
    public List<Course> getCoursesByIsBackup(boolean isBackup) {
        return courseDAO.findByIsBackup(isBackup);
    }

    @Override
    public boolean canSubmit(String roundId) {
        Optional<Round> round = roundDAO.findById(Long.valueOf(roundId));
        if (!round.isPresent())
            return false;
        if (new Date().after(round.get().getEndDate()))
            return false;
        return true;
    }

}
