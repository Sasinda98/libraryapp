package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.MUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    //Note to self: Ideally omit the use of like %whatever% in queries.
    @Query(value = "SELECT b FROM Book b WHERE b.title like %?1%")
    List<Book> searchForBookByTitle(String title);

    @Query(value = "SELECT u FROM MUser u\n" +
        "JOIN u.borrowedBooks b WHERE b.bookID = ?1")
    List<MUser> getBorrowersForABookByBookID(Long bookID);

    @Query(value = "SELECT CASE WHEN COUNT(u) > 0 \n" +
            "THEN TRUE \n" +
            "ELSE FALSE \n" +
            "END\n" +
            "FROM MUser u \n" +
            "JOIN u.borrowedBooks b  \n" +
            "WHERE u.userID = ?1 AND b.bookID = ?2")
    Boolean isBookBorrowedByUser(Long userID, Long bookID);
}
