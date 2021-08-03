package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(b) > 0 " +
            "THEN TRUE " +
            "ELSE FALSE " +
            "END " +
            "FROM Book b WHERE b.bookID = ?1")
    Boolean bookExistsByID(Long bookID);
}
