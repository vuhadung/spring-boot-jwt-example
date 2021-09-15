package com.fortna.hackathon.exception;

public class FileStorageException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -4697798449693765475L;

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}