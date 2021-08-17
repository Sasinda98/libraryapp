package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.AuthUserDetail;
import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.repositories.UserRepository;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

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
    public void updateUser(User User) throws OperationStoppedException {
        //check for user.
        Optional<User> optionalUser = userRepository.findById(User.getId());

        if(!optionalUser.isPresent())
            throw new NoResourceFoundException("User not found to update details.");

        userRepository.save(User);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksBorrowedByUserID(Long userID) {
        return userRepository.getBooksBorrowedByUserID(userID);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.orElseThrow(() -> new UsernameNotFoundException(""));

        UserDetails userDetails = new AuthUserDetail(userOptional.get());
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }
}
