package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class BookUserTest {

    //Test things common to both Book Repository and User Repository.

    private final BookRepository testBookRepository;
    private final UserRepository testUserRepository;

    @Autowired
    public BookUserTest(BookRepository testBookRepository, UserRepository testUserRepository) {
        this.testBookRepository = testBookRepository;
        this.testUserRepository = testUserRepository;
    }

    @Test
    void getBorrowersForABookByBookID() {
        //given
        Book book1 = new Book("Spring Framework for Dummies", "Educational", 1, 1111);
        Book book2 = new Book("JPA for Dummies", "Educational", 3, 2222);
        User user1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        user1.getBorrowedBooks().add(book1);
        book1.getBorrowers().add(user1);

        testUserRepository.save(user1);
        testBookRepository.saveAll(List.of(book1, book2));

        //when
        ArrayList<User> book1Borrowers = (ArrayList<User>) testBookRepository.getBorrowersForABookByBookID(book1.getBookID());
        ArrayList<User> book2Borrowers = (ArrayList<User>) testBookRepository.getBorrowersForABookByBookID(book2.getBookID());

        //Expect borrower to be user.
        Long borrowerCountBook1 = book1Borrowers
                .stream()
                .filter(u -> u.getUserID().equals(user1.getUserID()))
                .count();
        //Expect to have no borrowers.
        Long borrowerCountBook2 = book2Borrowers
                .stream()
                .count();

        //then
        assertThat(borrowerCountBook1).isEqualTo(1L);
        assertThat(borrowerCountBook2).isEqualTo(0L);
    }
}
