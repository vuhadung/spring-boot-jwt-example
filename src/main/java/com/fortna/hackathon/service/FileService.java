package com.fortna.hackathon.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public void storeFile(String dir, MultipartFile file);

    public CompletableFuture<Void> generateEntryPoint(String dir);
}
