package com.fortna.hackathon.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            org.springframework.security.access.AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        resolver.resolveException(request, response, null, accessDeniedException);
    }

}
