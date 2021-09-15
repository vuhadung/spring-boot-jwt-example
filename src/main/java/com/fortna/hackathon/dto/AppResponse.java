package com.fortna.hackathon.dto;

import java.io.Serializable;

public class AppResponse implements Serializable {

    private static final long serialVersionUID = -5355463006253310958L;
    
    private String error;
    
    private transient Object data;
    
    public AppResponse(String error, Object data) {
        this.error = error;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
