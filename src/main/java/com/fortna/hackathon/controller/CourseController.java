package com.fortna.hackathon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fortna.hackathon.dto.AppResponse;
import com.fortna.hackathon.entity.Course;
import com.fortna.hackathon.service.CourseService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllNormalCourses() {
        List<Course> courses = courseService.getCoursesByIsBackup(false);
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, courses));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/backup-course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBackupCourses() {
        List<Course> courses = courseService.getCoursesByIsBackup(true);
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, courses));
    }

}
