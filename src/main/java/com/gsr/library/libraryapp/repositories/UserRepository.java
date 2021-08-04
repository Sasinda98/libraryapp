package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(u) > 0 " +
            "THEN TRUE " +
            "ELSE FALSE " +
            "END " +
            "FROM User u WHERE u.email = ?1")
    Boolean checkIfUserExistByEmail(String email);

    @Query(value = "SELECT CASE WHEN COUNT(u) > 0 " +
            "THEN TRUE " +
            "ELSE FALSE " +
            "END " +
            "FROM User u WHERE u.userID = ?1")
    Boolean checkIfUserExistsByID(Long id);

    @Query(value = "SELECT b FROM Book b\n" +
            "JOIN b.borrowers u WHERE u.userID = ?1")
    List<Book> getBooksBorrowedByUserID(Long userID);
}
