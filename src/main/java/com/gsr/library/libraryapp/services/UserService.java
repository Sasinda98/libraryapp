package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByID(Long userID);
    void updateUser(User user) throws OperationStoppedException;
}
