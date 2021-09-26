package com.fortna.hackathon.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.fortna.hackathon.config.FileStorageConfiguration;
import com.fortna.hackathon.dao.CourseDao;
import com.fortna.hackathon.dao.LanguageDao;
import com.fortna.hackathon.dao.SubmissionDao;
import com.fortna.hackathon.dao.UserDao;
import com.fortna.hackathon.entity.Course;
import com.fortna.hackathon.entity.Language;
import com.fortna.hackathon.entity.Submission;
import com.fortna.hackathon.entity.User;
import com.fortna.hackathon.exception.FileStorageException;
import com.fortna.hackathon.service.FileService;

@Service(value = "fileService")
@Transactional
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private LanguageDao languageDAO;

    @Autowired
    private SubmissionDao submissionDAO;

    @Autowired
    private UserDao userDAO;

    @Autowired
    private CourseDao courseDAO;

    @Autowired
    private FileStorageConfiguration fileStorageConfig;

    public void storePlayerFile(String dir, String langId, MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetDir = Paths.get(this.fileStorageConfig.getPlayerDir(), dir).toAbsolutePath().normalize();
            Files.createDirectories(targetDir);
            // PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxrwxrwx"))

            Optional<Language> lang = languageDAO.findById(Long.valueOf(langId));
            if (!lang.isPresent()) {
                throw new FileStorageException("Sorry! Language type is not suppored");
            }

            String newFileName = null;
            if (lang.get().getName().toLowerCase().contains("java")) {
                // Java
                String[] extensions = fileName.split(Pattern.quote("."));
                if (extensions.length >= 2 && !"jar".equals(extensions[extensions.length - 1])) {
                    throw new FileStorageException("Sorry! This extension is not allowed to upload!" + fileName);
                }
                newFileName = "runme" + "." + extensions[extensions.length - 1];
            } else {
                // Cpp
                String[] extensions = fileName.split(Pattern.quote("."));
                if (extensions.length >= 2) {
                    throw new FileStorageException("Sorry! This extension is not allowed to upload!" + fileName);
                }
                newFileName = "runme";
            }

            Path targetLocation = targetDir.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // update to DB
            User user = userDAO.findByUsername(dir);
            Submission submission = submissionDAO.findByUserId(user.getId());
            if (submission == null) {
                submission = new Submission();
                submission.setCreatedDate(new Date());
                submission.setLanguage(lang.get());
                submission.setUser(user);
                submission.setPathToFile(targetLocation.toString());
                submission.setUpdatedDate(new Date());
            } else {
                submission.setLanguage(lang.get());
                submission.setPathToFile(targetLocation.toString());
                submission.setUpdatedDate(new Date());
            }
            submissionDAO.save(submission);

            return;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Async("asyncExecutor")
    public void generateEntryPoint(String dir, String langId) {
        Optional<Language> lang = languageDAO.findById(Long.valueOf(langId));
        if (!lang.get().getName().toLowerCase().contains("java")) {
            return;
        } else {
            Path targetDir = Paths.get(this.fileStorageConfig.getPlayerDir(), dir).toAbsolutePath().normalize();
            String shFile = new StringBuilder().append(targetDir.toString()).append(File.separator).append("runme.sh")
                    .toString();
            String jarFile = new StringBuilder().append(targetDir.toString()).append(File.separator).append("runme.jar")
                    .toString();
            String command = new StringBuilder().append("java -jar ").append(jarFile).toString();
            String content = new StringBuilder().append("#!/bin/bash").append("\n").append(command).toString();

            try (BufferedWriter br = new BufferedWriter(new FileWriter(new File(shFile)))) {
                br.write(content);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

            ProcessBuilder processBuilder = null;
            Process process = null;
            try {
                processBuilder = new ProcessBuilder("chmod", "+x", shFile);
                process = processBuilder.start();
                process.waitFor();

            } catch (IOException | InterruptedException e) {
                logger.error(e.getMessage());
            } finally {
                process.destroy();
            }

            return;
        }
    }

    public void storeCourseFile(String courseName, MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetDir = Paths.get(this.fileStorageConfig.getCourseDir()).toAbsolutePath().normalize();
            Files.createDirectories(targetDir);
            // PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxrwxrwx"))

            String newFileName = null;
            String[] extensions = fileName.split(Pattern.quote("."));
            if (extensions.length >= 2 && !"smrjky".equals(extensions[extensions.length - 1])) {
                throw new FileStorageException("Sorry! This extension is not allowed to upload!");
            }

            Course course = new Course();
            course.setName(courseName);
            course.setCreatedDate(new Date());
            course = courseDAO.save(course);

            newFileName = "course_" + course.getId() + "." + extensions[extensions.length - 1];

            Path targetLocation = targetDir.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            course.setPathToFile(targetLocation.toString());
            course.setUpdatedDate(new Date());
            courseDAO.save(course);

            return;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }

    }

    public void storeUserAvatar(String userName, MultipartFile file) {
        if (file.getContentType() == null || file.getContentType().isEmpty()
                || !file.getContentType().startsWith("image")) {
            throw new FileStorageException("Sorry! This extension is not allowed to upload!");
        }

        if (file.getSize() > 520_000L) {
            throw new MaxUploadSizeExceededException(520_000L);
        }

        User user = userDAO.findByUsername(userName);
        try {
            user.setAvatar(file.getBytes());
            userDAO.save(user);
        } catch (IOException e) {
            logger.error("Can not save user avatar: {}", e.getMessage());
        }

    }

}
