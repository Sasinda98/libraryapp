package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.repositories.UserRepository;
import jdk.javadoc.internal.doclets.toolkit.util.ResourceIOException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers(){
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByID(Long userID) {
        return userRepository.findById(userID);
    }

    @Override
    @Transactional
    public void updateUser(User user) throws OperationStoppedException {
        //check for user.
        Optional<User> optionalUser = userRepository.findById(user.getUserID());

        if(!optionalUser.isPresent())
            throw new NoResourceFoundException("User not found to update details.");

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksBorrowedByUserID(Long userID) {
        return userRepository.getBooksBorrowedByUserID(userID);
    }
}
