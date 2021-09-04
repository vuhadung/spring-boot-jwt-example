package com.fortna.hackathon.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<?> handlAnthenticationException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized!");
    }
    
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    protected ResponseEntity<?> handleAccessDeniedException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden!");
    }

}
