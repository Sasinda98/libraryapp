package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.BeforeEach;
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

    private Book book1 = new Book("Spring Framework for Dummies", "Educational", 1, 1111);
    private Book book2 = new Book("JPA for Dummies", "Educational", 3, 2222);
    private User user1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
    private User user2 = new User("John", "Doe", "john.doe@domain.com");

    @Autowired
    public BookUserTest(BookRepository testBookRepository, UserRepository testUserRepository) {
        this.testBookRepository = testBookRepository;
        this.testUserRepository = testUserRepository;
    }

    @BeforeEach
    void setUp() {
        user1.getBorrowedBooks().add(book1);
        book1.getBorrowers().add(user1);

        testUserRepository.saveAll(List.of(user1, user2));
        testBookRepository.saveAll(List.of(book1, book2));
    }

    @Test
    void getBorrowersForABookByBookID() {
        //given -> see setUp()

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

    @Test
    void getBooksBorrowedByUserID() {
        //given -> see setUp()

        //when
        ArrayList<Book> user1Books = (ArrayList<Book>) testUserRepository.getBooksBorrowedByUserID(user1.getUserID());
        ArrayList<Book> user2Books = (ArrayList<Book>) testUserRepository.getBooksBorrowedByUserID(user2.getUserID());

        Long user1BookCount = user1Books
                .stream()
                .filter(book -> book1.getBookID().equals(book.getBookID()))
                .count();
        Long user2BookCount = user2Books
                .stream()
                .count();
        //then
        assertThat(user1BookCount).isEqualTo(1L);
        assertThat(user2BookCount).isEqualTo(0L);
    }
}
