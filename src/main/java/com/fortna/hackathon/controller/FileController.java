package com.fortna.hackathon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fortna.hackathon.dto.AppResponse;
import com.fortna.hackathon.service.CourseService;
import com.fortna.hackathon.service.FileService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@PreAuthorize("isAuthenticated()")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private CourseService courseService;

    /**
     * 
     * @param roundId
     * @param langId
     * @param file
     * @return
     */
    @PostMapping(value = "/player/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPlayerFile(@RequestPart(required = true, name = "language") String langId,
            @RequestPart(required = true, name = "round") String roundId,
            @RequestPart(required = true, name = "file") MultipartFile file) {

        if (!courseService.canSubmit(roundId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AppResponse("Can not submit for this round", null));
        }

        if (file.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse("File upload is empty!", null));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        fileService.storePlayerFile(authentication.getName(), langId, file);
        fileService.generateEntryPoint(authentication.getName(), langId);
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, "Upload successfully!"));
    }

    /**
     * 
     * @param courseName
     * @param file
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/course/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadCourseFile(@RequestPart(required = true, name = "name") String courseName,
            @RequestPart(required = true, name = "isBackup") String isBackup,
            @RequestPart(required = true, name = "file") MultipartFile file) {
        if ("0".equals(isBackup)) {
            fileService.storeCourseFile(courseName, false, file);
        } else if ("1".equals(isBackup)) {
            fileService.storeCourseFile(courseName, true, file);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse("Invalid params!", null));
        }
        if (file.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse("File upload is empty!", null));
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, "Upload successfully!"));
    }

    @PostMapping(value = "/user/avatar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadUserAvatar(@RequestPart(required = true, name = "file") MultipartFile file) {
        if (file.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppResponse("File upload is empty!", null));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        fileService.storeUserAvatar(authentication.getName(), file);
        return ResponseEntity.status(HttpStatus.OK).body(new AppResponse(null, "Upload successfully!"));
    }

}
