package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.exceptions.ValidationException;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Boolean bookExistsByID(Long bookID);

    Boolean isBookBorrowedByUser(Long userID, Long bookID);

    List<Book> searchForBookByTitle(String title);

    List<User> getBorrowersForABookByBookID(Long bookID);

    void updateBook(Book book) throws ValidationException, OperationStoppedException;

    void deleteBook(Long bookID) throws OperationStoppedException;
}
