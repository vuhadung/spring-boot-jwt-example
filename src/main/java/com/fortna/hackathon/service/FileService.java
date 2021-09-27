package com.fortna.hackathon.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void storePlayerFile(String dir, String langId, MultipartFile file);

    void generateEntryPoint(String dir, String langId);
    
    void storeCourseFile(String courseName, String deadline, MultipartFile file);
    
    void storeUserAvatar(String userName, MultipartFile file);
    
}
