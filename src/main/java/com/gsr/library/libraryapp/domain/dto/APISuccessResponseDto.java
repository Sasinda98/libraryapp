package com.gsr.library.libraryapp.domain.dto;

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
}
