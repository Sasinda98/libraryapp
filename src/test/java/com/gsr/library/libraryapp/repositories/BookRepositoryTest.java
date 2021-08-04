package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BookRepositoryTest {

    private final BookRepository testBookRepository;

    @Autowired
    public BookRepositoryTest(BookRepository testBookRepository) {
        this.testBookRepository = testBookRepository;
    }

    @Test
    void searchForBookByTitleExactMatch() {
        //given
        Book book1 = new Book("Spring Framework for Dummies", "Educational", 1, 1111);
        Book book2 = new Book("JPA for Dummies", "Educational", 3, 2222);
        testBookRepository.saveAll(List.of(book1, book2));

        //when
        ArrayList<Book> queryResult = (ArrayList<Book>) testBookRepository.searchForBookByTitle(book1.getTitle());

        //then
        assertThat(queryResult.size()).isEqualTo(1);
        assertThat(queryResult
                .stream()
                .findFirst()).hasValue(book1);
    }

    @Test
    void searchForBookByTitleApproxMatch() {
        //given
        Book book1 = new Book("Spring Framework for Dummies", "Educational", 1, 1111);
        Book book2 = new Book("JPA for Dummies", "Educational", 3, 2222);
        testBookRepository.saveAll(List.of(book1, book2));

        //when
        ArrayList<Book> queryResult = (ArrayList<Book>) testBookRepository.searchForBookByTitle(book1.getTitle().substring(2,7));

        //then
        assertThat(queryResult.size()).isEqualTo(1);
        assertThat(queryResult
                .stream()
                .findFirst()).hasValue(book1);
    }
}