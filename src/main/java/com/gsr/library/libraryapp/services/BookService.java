package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();

    Optional<Book> bookExistsByID(Long bookID);

    Boolean isBookBorrowedByUser(Long userID, Long bookID);

    List<Book> searchForBookByTitle(String title);

    List<User> getBorrowersForABookByBookID(Long bookID);

    void updateBook(Book book) throws ValidationException, OperationStoppedException;

    void deleteBook(Long bookID) throws OperationStoppedException;

    void borrowBook(Long userID, Long bookID) throws OperationStoppedException;

    void returnBook(Long userID, Long bookID) throws OperationStoppedException;
}
