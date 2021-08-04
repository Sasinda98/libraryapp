package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    //Get mocked version of book repository.
    @Mock
    private BookRepository bookRepository;
    private BookServiceImpl testBookServiceImpl;

    @BeforeEach
    void setUp() {
        //Provide the mocked version of book repository to the service.
        testBookServiceImpl = new BookServiceImpl(bookRepository);
    }

    /**
     * Verify that the book service invokes the method findAll() in the repository.
     * Mocking allows to run the tests without the need for an actual db/repository functionality underneath.
     */
    @Test
    void getAllBooks() {
        //when
        testBookServiceImpl.getAllBooks();
        //then
        verify(bookRepository).findAll();
    }

    /***
     * Verify that the book service invokes searchForBookByTitle(string) in the repository.
     */
    @Test
    void searchForBookByTitle(){
        //when
        String title = anyString();
        testBookServiceImpl.searchForBookByTitle(title);

        //then
        ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
        verify(bookRepository).searchForBookByTitle(titleCaptor.capture());

        String titleParam = titleCaptor.getValue();

        assertThat(titleParam).isEqualTo(title);
    }

    @Test
    void bookExistsByID(){
        //given
        Long bookID = 1L;

        //when
        testBookServiceImpl.bookExistsByID(bookID);

        //then
        ArgumentCaptor<Long> bookIDCaptor = ArgumentCaptor.forClass(Long.class);
        verify(bookRepository).findById(bookIDCaptor.capture());

        Long capturedBookID = bookIDCaptor.getValue();

        assertThat(capturedBookID).isEqualTo(bookID);
    }

    @Test
    void getBorrowersForABookByBookID(){
        //given
        Long bookID = 1L;

        //When
        testBookServiceImpl.getBorrowersForABookByBookID(bookID);

        //then
        ArgumentCaptor<Long> bookIDCaptor = ArgumentCaptor.forClass(Long.class);
        verify(bookRepository).getBorrowersForABookByBookID(bookIDCaptor.capture());

        Long capturedBookID = bookIDCaptor.getValue();

        assertThat(capturedBookID).isEqualTo(bookID);
    }

    @Test
    void updateBook(){
        //given
        Book validBook = new Book("Garry", "Fiction", 3, 1234);
        validBook.setBookID(1L);

        Optional<Book> bookOptional = Optional.of(validBook);
        given(bookRepository
                .findById(validBook.getBookID()))
                .willReturn(bookOptional);

        //when
        testBookServiceImpl.updateBook(validBook);

        //then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());

        Book capturedBook = bookArgumentCaptor.getValue();

        assertThat(capturedBook).isEqualTo(validBook);
    }

    @Test
    void updateBookThrowsOperationStoppedException(){
        //given
        Book validBook = new Book("Garry", "Fiction", 3, 1234);
        validBook.setBookID(1L);

        /***
         * Not setting the optional to be returned by bookRepository.findById(book.getBookID()) causes the optional
         * to return false when bookOptional.isPresent() is called. Which in turn causes
         * OperationStoppedException to be thrown by the actual updateBook(...)
         * -- Summary: Simulates a book not being there in the db to actually update it.--
         */

        //when and then
        assertThatThrownBy(() -> testBookServiceImpl.updateBook(validBook))
                .isExactlyInstanceOf(OperationStoppedException.class)
                .hasMessage("No such book found to update.");

    }

    @Test
    void updateBookThrowsValidationException(){
        //given
        Book invalidBook = new Book("", "", -999, -59);
        invalidBook.setBookID(1L);

        Optional<Book> bookOptional = Optional.of(invalidBook);
        given(bookRepository
                .findById(invalidBook.getBookID()))
                .willReturn(bookOptional);

        //when and then
        assertThatThrownBy(() -> testBookServiceImpl.updateBook(invalidBook))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage("Invalid book details.");

    }

    @Test
    void deleteBook(){
        //given
        Book validBook = new Book("Garry", "Fiction", 3, 1234);
        Long bookID = 1L;
        validBook.setBookID(bookID);

        Optional<Book> bookOptional = Optional.of(validBook);
        given(bookRepository
                .findById(validBook.getBookID()))
                .willReturn(bookOptional);
        //when
        testBookServiceImpl.deleteBook(bookID);

        //then
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).delete(bookCaptor.capture());
        Book bookCaptured = bookCaptor.getValue();
        assertThat(bookCaptured).isEqualTo(validBook);
    }

    @Test
    void deleteBookThrowsOperationStoppedException(){
        //given
        Book validBook = new Book("Garry", "Fiction", 3, 1234);
        Long bookID = 1L;
        validBook.setBookID(bookID);

        /***
         * Not setting the optional to be returned by bookRepository.findById(book.getBookID()) causes the optional
         * to return false when bookOptional.isPresent() is called. Which in turn causes
         * OperationStoppedException to be thrown by the actual deleteBook(...)
         * -- Summary: Simulates a book not being there in the db to actually delete it.--
         */

        //when and then
        assertThatThrownBy(() -> testBookServiceImpl.deleteBook(bookID))
                .isExactlyInstanceOf(OperationStoppedException.class)
                .hasMessage("No such book found to delete.");

    }

    @Test
    void isBookBorrowedByUser(){
        //given
        Long userID = 1L;
        Long bookID = 2L;

        //when
        testBookServiceImpl.isBookBorrowedByUser(userID, bookID);

        //then
        ArgumentCaptor<Long> userIDCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> bookIDCaptor = ArgumentCaptor.forClass(Long.class);

        verify(bookRepository).isBookBorrowedByUser(userIDCaptor.capture(), bookIDCaptor.capture());

        Long capturedUserID = userIDCaptor.getValue();
        Long capturedBookID = bookIDCaptor.getValue();

        assertThat(capturedBookID).isEqualTo(bookID);
        assertThat(capturedUserID).isEqualTo(userID);
    }
}