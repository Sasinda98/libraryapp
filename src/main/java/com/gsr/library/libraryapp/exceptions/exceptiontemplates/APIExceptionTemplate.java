package com.gsr.library.libraryapp.exceptions.exceptiontemplates;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        APIExceptionTemplate that = (APIExceptionTemplate) o;
        return Objects.equals(message, that.message) && Objects.equals(error, that.error) && Objects.equals(status, that.status) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, error, status, timestamp);
    }

    @Override
    public String toString() {
        return "APIExceptionTemplate{" +
                "message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }
}
