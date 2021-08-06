package com.gsr.library.libraryapp.exceptions.exceptiontemplates;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class APIExceptionTemplate {
    private String message;
    private String error;
    private Integer status;
    private Date timestamp;

    public APIExceptionTemplate() {
    }

    public APIExceptionTemplate(String message, String error, Integer status, Date timestamp) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
