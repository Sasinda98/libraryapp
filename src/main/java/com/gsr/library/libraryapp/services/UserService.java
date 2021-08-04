package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByID(Long userID);
}
