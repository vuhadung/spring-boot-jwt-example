package com.fortna.hackathon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fortna.hackathon.dao.CourseDao;
import com.fortna.hackathon.dao.RoundDao;
import com.fortna.hackathon.dto.CreateRoundDto;
import com.fortna.hackathon.entity.Course;
import com.fortna.hackathon.entity.Round;
import com.fortna.hackathon.service.RoundService;

@Service(value = "roundService")
@Transactional
public class RoundServiceImpl implements RoundService {

    @Autowired
    private RoundDao roundDao;

    @Autowired
    private CourseDao courseDao;

    @Override
    public boolean createRound(CreateRoundDto roundDto) {
        Optional<Course> mainCourse = courseDao.findById(roundDto.getMainCourseId());
        Optional<Course> backupCourse = courseDao.findById(roundDto.getBackupCourseId());

        if (!mainCourse.isPresent() || !backupCourse.isPresent())
            return false;

        Round round = new Round();
        round.setName(roundDto.getRoundName());
        round.setStartDate(new Date(roundDto.getStartDate() * 1000));
        round.setEndDate(new Date(roundDto.getEndDate() * 1000));
        round.setCourse(mainCourse.get());
        round.setBackupCourse(backupCourse.get());
        roundDao.save(round);
        return true;
    }

    @Override
    public List<Round> getActiveRounds() {
        return roundDao.findActiveRounds();
    }

    @Override
    public List<Round> getAllRounds() {
        return roundDao.findAllByOrderByStartDateAsc();
    }

}
