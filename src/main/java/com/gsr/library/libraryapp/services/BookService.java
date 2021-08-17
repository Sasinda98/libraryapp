package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.MUser;
import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();

    Optional<Book> bookExistsByID(Long bookID);

    Boolean isBookBorrowedByUser(Long userID, Long bookID);

    List<Book> searchForBookByTitle(String title);

    List<MUser> getBorrowersForABookByBookID(Long bookID);

    Book updateBook(Book book) throws ValidationException, NoResourceFoundException;

    Book deleteBook(Long bookID) throws OperationStoppedException, NoResourceFoundException;

    Boolean borrowBook(Long userID, Long bookID) throws OperationStoppedException, NoResourceFoundException;

    Boolean returnBook(Long userID, Long bookID) throws OperationStoppedException, NoResourceFoundException;
}
