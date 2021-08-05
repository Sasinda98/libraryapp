package com.gsr.library.libraryapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UserDto {

    @JsonProperty("user_id")
    private Long userID;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("modified_at")
    private Date modifiedAt;

    public UserDto() {
    }

    public UserDto(Long userID, String firstName, String lastName, String email, Date createdAt, Date modifiedAt) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
