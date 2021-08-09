package com.gsr.library.libraryapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class ListUserDto {
    @JsonProperty("number_of_users")
    private Integer numberOfUsers;
    @JsonProperty("users")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListUserDto that = (ListUserDto) o;
        return Objects.equals(numberOfUsers, that.numberOfUsers) && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfUsers, users);
    }

    @Override
    public String toString() {
        return "ListUserDto{" +
                "numberOfUsers=" + numberOfUsers +
                ", users=" + users +
                '}';
    }
}
