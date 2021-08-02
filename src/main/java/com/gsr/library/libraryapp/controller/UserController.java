package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.repositories.UserRepository;
import com.gsr.library.libraryapp.services.UserService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public List<User> getUsers(){
        return (List<User>) userService.getUsers();
    }

}

