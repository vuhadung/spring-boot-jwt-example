package com.fortna.hackathon.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fortna.hackathon.dto.AppResponse;
import com.fortna.hackathon.exception.FileStorageException;
import com.fortna.hackathon.exception.RunGameException;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<?> handlAnthenticationException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AppResponse("Unauthorized!", null));
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    protected ResponseEntity<?> handleAccessDeniedException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AppResponse("Forbidden!", null));
    }

    @ExceptionHandler(FileStorageException.class)
    protected ResponseEntity<?> handleUploadFileException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AppResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(RunGameException.class)
    protected ResponseEntity<?> handleExecuteGameException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AppResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<?> handleMaxSizeException(RuntimeException ex, WebRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AppResponse("File is too large!", null));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(new AppResponse("Validation failed!", errors), HttpStatus.BAD_REQUEST);
    }

}
