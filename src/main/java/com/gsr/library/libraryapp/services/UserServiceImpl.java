package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.MUser;
import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.repositories.UserRepository;
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
    public List<MUser> getUsers(){
        return (List<MUser>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MUser> getUserByID(Long userID) {
        return userRepository.findById(userID);
    }

    @Override
    @Transactional
    public void updateUser(MUser MUser) throws OperationStoppedException {
        //check for user.
        Optional<MUser> optionalUser = userRepository.findById(MUser.getUserID());

        if(!optionalUser.isPresent())
            throw new NoResourceFoundException("User not found to update details.");

        userRepository.save(MUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksBorrowedByUserID(Long userID) {
        return userRepository.getBooksBorrowedByUserID(userID);
    }
}
