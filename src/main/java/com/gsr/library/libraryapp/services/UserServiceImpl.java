package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return (List<User>) userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByID(Long userID) {
        return userRepository.findById(userID);
    }
}
