package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(b) > 0 " +
            "THEN TRUE " +
            "ELSE FALSE " +
            "END " +
            "FROM Book b WHERE b.bookID = ?1")
    Boolean bookExistsByID(Long bookID);

    //Note to self: Ideally omit the use of like %whatever% in queries.
    @Query(value = "SELECT b FROM Book b WHERE b.title like %?1%")
    List<Book> searchForBookByTitle(String title);

    @Query(value = "SELECT u FROM User u\n" +
        "JOIN u.borrowedBooks b WHERE b.bookID = ?1")
    List<User> getBorrowersForABookByBookID(Long bookID);
}
