package com.gsr.library.libraryapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListUserDto {
    @JsonProperty("number_of_users")
    private Integer numberOfUsers;
    private List<UserDto> users;

    public ListUserDto() {
    }

    public ListUserDto(Integer numberOfUsers, List<UserDto> users) {
        this.numberOfUsers = numberOfUsers;
        this.users = users;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }
}
