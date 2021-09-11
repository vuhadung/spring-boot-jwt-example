package com.fortna.hackathon.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public void storeFile(MultipartFile file);

}
