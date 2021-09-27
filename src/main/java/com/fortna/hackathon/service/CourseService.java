package com.fortna.hackathon.service;

import java.util.List;

import com.fortna.hackathon.entity.Course;

public interface CourseService {

    public List<Course> getAllCourses();

    public List<Course> getCoursesByIsBackup(boolean isBackup);
    
    public boolean canSubmitForCourse(String courseId);

}
