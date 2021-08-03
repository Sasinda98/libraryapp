package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BookRepositoryTest {

    private final BookRepository testBookRepository;

    @Autowired
    public BookRepositoryTest(BookRepository testBookRepository) {
        this.testBookRepository = testBookRepository;
    }

    @Test
    void checkForABookThatExistsByID() {
        //given
        Book book = new Book("Spring Framework for Dummies", "Educational", 1,9999);
        Long bookID = testBookRepository.save(book).getBookID();
        //when
        boolean bookExists = testBookRepository.bookExistsByID(bookID);
        //then
        assertThat(bookExists).isTrue();
    }

    @Test
    void checkForABookThatDoesNotExistsByID() {
        //given
        Long bookID = 1L;
        //when
        boolean bookExists = testBookRepository.bookExistsByID(bookID);
        //then
        assertThat(bookExists).isFalse();
    }
    
}