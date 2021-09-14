package com.fortna.hackathon.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fortna.hackathon.config.FileStorageConfiguration;
import com.fortna.hackathon.exception.FileStorageException;
import com.fortna.hackathon.service.FileService;

@Service(value = "fileService")
public class FileServiceImpl implements FileService {

    @Autowired
    private FileStorageConfiguration fileStorageConfig;

    public void storeFile(String dir, MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetDir = Paths.get(this.fileStorageConfig.getUploadDir(), dir).toAbsolutePath().normalize();
            Files.createDirectories(targetDir);
            // PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxrwxrwx"))

            String[] extensions = fileName.split(Pattern.quote("."));
            String newFileName = "file" + "." + extensions[extensions.length - 1];

            Path targetLocation = targetDir.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> generateEntryPoint(String dir) {
        Path targetDir = Paths.get(this.fileStorageConfig.getUploadDir(), dir).toAbsolutePath().normalize();
        String shFile = new StringBuilder().append(targetDir.toString()).append(File.separator).append("runme.sh")
                .toString();
        String jarFile = new StringBuilder().append(targetDir.toString()).append(File.separator).append("runme.jar")
                .toString();
        String command = new StringBuilder().append("java -jar ").append(jarFile).toString();
        String content = new StringBuilder().append("#!/bin/bash").append("\n").append(command).toString();

        try (BufferedWriter br = new BufferedWriter(new FileWriter(new File(shFile)))) {
            br.write(content);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ProcessBuilder processBuilder = null;
        Process process = null;
        try {
            processBuilder = new ProcessBuilder("chmod", "+x", shFile);
            process = processBuilder.start();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            process.destroy();
        }

        return null;
    }

}
