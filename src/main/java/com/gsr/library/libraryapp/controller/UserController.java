package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.repositories.UserRepository;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return (List<User>) userRepository.findAll();
    }
}

