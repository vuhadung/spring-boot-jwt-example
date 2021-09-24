package com.fortna.hackathon.aspect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SwitchOff {
    
    public String epochTime();

}
