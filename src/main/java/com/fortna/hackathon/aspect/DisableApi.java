package com.fortna.hackathon.aspect;

import java.lang.reflect.Method;
import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.fortna.hackathon.dto.AppResponse;

@Aspect
@Component
public class DisableApi {

    private static final Logger logger = LoggerFactory.getLogger(DisableApi.class);

    @Around("@annotation(com.fortna.hackathon.aspect.SwitchOff)")
    public Object advise(ProceedingJoinPoint call) throws Throwable {
        MethodSignature signature = (MethodSignature) call.getSignature();
        Method method = signature.getMethod();
        SwitchOff switchOff = method.getAnnotation(SwitchOff.class);
        Long epochTime = Long.valueOf(switchOff.epochTime());
        logger.info("This api can not be invoke after epoch time {}", epochTime);
        if (Instant.now().isAfter(Instant.ofEpochSecond(epochTime))) {
            logger.info("Can not call this api right now!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AppResponse("Not found!", null));
        } else {
            return call.proceed();
        }
    }
}
