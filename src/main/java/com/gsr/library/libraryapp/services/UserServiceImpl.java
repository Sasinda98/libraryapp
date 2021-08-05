package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
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

    @Override
    public List<User> getUsers(){
        return (List<User>) userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByID(Long userID) {
        return userRepository.findById(userID);
    }

    @Override
    public void updateUser(User user) throws OperationStoppedException {
        //check for user.
        Optional<User> optionalUser = userRepository.findById(user.getUserID());

        if(!optionalUser.isPresent())
            throw new OperationStoppedException("User not found to update details.");

        userRepository.save(user);
    }

    @Override
    public List<Book> getBooksBorrowedByUserID(Long userID) throws OperationStoppedException {
        //check for user.
        Optional<User> optionalUser = userRepository.findById(userID);

        if(!optionalUser.isPresent())
            throw new OperationStoppedException("No such user to present borrowed info.");

        return userRepository.getBooksBorrowedByUserID(userID);
    }
}
