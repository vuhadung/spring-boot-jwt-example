package com.fortna.hackathon.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public void storeFile(String dir, MultipartFile file);

}
