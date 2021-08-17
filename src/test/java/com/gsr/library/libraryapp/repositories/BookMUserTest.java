package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.MUser;
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
public class BookMUserTest {

    //Test things common to both Book Repository and User Repository.

    private final BookRepository testBookRepository;
    private final UserRepository testUserRepository;

    private Book book1 = new Book("Spring Framework for Dummies", "Educational", 1, 1111);
    private Book book2 = new Book("JPA for Dummies", "Educational", 3, 2222);
    private MUser MUser1 = new MUser("Gayal", "Rupasinghe", "gayal@domain.com");
    private MUser MUser2 = new MUser("John", "Doe", "john.doe@domain.com");

    @Autowired
    public BookMUserTest(BookRepository testBookRepository, UserRepository testUserRepository) {
        this.testBookRepository = testBookRepository;
        this.testUserRepository = testUserRepository;
    }

    @BeforeEach
    void setUp() {
        MUser1.getBorrowedBooks().add(book1);
        book1.getBorrowers().add(MUser1);

        testUserRepository.saveAll(List.of(MUser1, MUser2));
        testBookRepository.saveAll(List.of(book1, book2));
    }

    @Test
    void getBorrowersForABookByBookID() {
        //given -> see setUp()

        //when
        ArrayList<MUser> book1Borrowers = (ArrayList<MUser>) testBookRepository.getBorrowersForABookByBookID(book1.getBookID());
        ArrayList<MUser> book2Borrowers = (ArrayList<MUser>) testBookRepository.getBorrowersForABookByBookID(book2.getBookID());

        //Expect borrower to be user.
        Long borrowerCountBook1 = book1Borrowers
                .stream()
                .filter(u -> u.getUserID().equals(MUser1.getUserID()))
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
        ArrayList<Book> user1Books = (ArrayList<Book>) testUserRepository.getBooksBorrowedByUserID(MUser1.getUserID());
        ArrayList<Book> user2Books = (ArrayList<Book>) testUserRepository.getBooksBorrowedByUserID(MUser2.getUserID());

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

    @Test
    void isBookBorrowedByUser(){
        //given -> see setUp()

        //when
        Boolean isBookBorrowedByUser1 = testBookRepository.isBookBorrowedByUser(MUser1.getUserID(), book1.getBookID());
        Boolean isBookBorrowedByUser2 = testBookRepository.isBookBorrowedByUser(MUser2.getUserID(), book1.getBookID());

        //then
        assertThat(isBookBorrowedByUser1).isTrue();
        assertThat(isBookBorrowedByUser2).isFalse();
    }
}
