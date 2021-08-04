package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.services.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
    @GetMapping
    public List<User> getUsers(){
        return userServiceImpl.getUsers();
    }

}

