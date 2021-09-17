package com.fortna.hackathon.exception;

public class RunGameException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -3175551086079216960L;

    public RunGameException(String message) {
        super(message);
    }

    public RunGameException(String message, Throwable cause) {
        super(message, cause);
    }

}
