package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByID(Long userID);
    List<User> getUsers();
    void updateUser(User User) throws OperationStoppedException;
    List<Book> getBooksBorrowedByUserID(Long userID);
}
