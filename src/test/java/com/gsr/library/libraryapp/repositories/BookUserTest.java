package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class BookUserTest {

    //Test things common to both Book Repository and User Repository.

    private final BookRepository testBookRepository;
    private final UserRepository testUserRepository;

    private Book book1 = new Book("Spring Framework for Dummies", "Educational", 1, 1111);
    private Book book2 = new Book("JPA for Dummies", "Educational", 3, 2222);
    private User User1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
    private User User2 = new User("John", "Doe", "john.doe@domain.com");

    @Autowired
    public BookUserTest(BookRepository testBookRepository, UserRepository testUserRepository) {
        this.testBookRepository = testBookRepository;
        this.testUserRepository = testUserRepository;
    }

    @BeforeEach
    void setUp() {
        User1.getBorrowedBooks().add(book1);
        book1.getBorrowers().add(User1);

        testUserRepository.saveAll(List.of(User1, User2));
        testBookRepository.saveAll(List.of(book1, book2));
    }

    @Test
    void getBorrowersForABookByBookID() {
        //given -> see setUp()

        //when
        ArrayList<User> book1Borrowers = (ArrayList<User>) testBookRepository.getBorrowersForABookByBookID(book1.getId());
        ArrayList<User> book2Borrowers = (ArrayList<User>) testBookRepository.getBorrowersForABookByBookID(book2.getId());

        //Expect borrower to be user.
        Long borrowerCountBook1 = book1Borrowers
                .stream()
                .filter(u -> u.getId().equals(User1.getId()))
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
        ArrayList<Book> user1Books = (ArrayList<Book>) testUserRepository.getBooksBorrowedByUserID(User1.getId());
        ArrayList<Book> user2Books = (ArrayList<Book>) testUserRepository.getBooksBorrowedByUserID(User2.getId());

        Long user1BookCount = user1Books
                .stream()
                .filter(book -> book1.getId().equals(book.getId()))
                .count();
        Long user2BookCount = user2Books
                .stream()
                .count();
        //then
        assertThat(user1BookCount).isEqualTo(1L);
        assertThat(user2BookCount).isEqualTo(0L);
    }

    @Test
    void isBookBorrowedByUser(){
        //given -> see setUp()

        //when
        Boolean isBookBorrowedByUser1 = testBookRepository.isBookBorrowedByUser(User1.getId(), book1.getId());
        Boolean isBookBorrowedByUser2 = testBookRepository.isBookBorrowedByUser(User2.getId(), book1.getId());

        //then
        assertThat(isBookBorrowedByUser1).isTrue();
        assertThat(isBookBorrowedByUser2).isFalse();
    }
}
