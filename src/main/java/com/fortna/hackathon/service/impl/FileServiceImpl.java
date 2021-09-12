package com.fortna.hackathon.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;

import org.springframework.beans.factory.annotation.Autowired;
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
            Files.createDirectories(targetDir,
                    PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxrwxrwx")));

            Path targetLocation = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
