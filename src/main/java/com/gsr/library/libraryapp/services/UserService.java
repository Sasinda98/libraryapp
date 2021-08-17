package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.MUser;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<MUser> getUserByID(Long userID);
    List<MUser> getUsers();
    void updateUser(MUser MUser) throws OperationStoppedException;
    List<Book> getBooksBorrowedByUserID(Long userID);
}
