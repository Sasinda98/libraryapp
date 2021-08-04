package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Boolean bookExistsByID(Long bookID);

    Boolean isBookBorrowedByUser(Long userID, Long bookID);

    List<Book> searchForBookByTitle(String title);

    List<User> getBorrowersForABookByBookID(Long bookID);
}
