package com.gsr.library.libraryapp.domain.dto;

import java.util.Objects;

public class APISuccessResponseDto {
    private String message;

    public APISuccessResponseDto() {
    }

    public APISuccessResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        APISuccessResponseDto that = (APISuccessResponseDto) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return "APISuccessResponseDto{" +
                "message='" + message + '\'' +
                '}';
    }
}
