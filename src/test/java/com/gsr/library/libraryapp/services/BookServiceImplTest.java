package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
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
    private BookService testBookServiceImpl;
    @Mock
    private UserService userService;
    private ModelMapper modelMapper = new ModelMapper();


    @BeforeEach
    void setUp() {
        //Provide the mocked version of book repository to the service.
        testBookServiceImpl = new BookServiceImpl(bookRepository, userService, modelMapper);
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
        Date oldModifiedDate = validBook.getModifiedAt();
        validBook.setBookID(1L);

        Optional<Book> bookOptional = Optional.of(validBook);
        given(bookRepository
                .findById(validBook.getBookID()))
                .willReturn(bookOptional);

        //when
        Book returnedBook = testBookServiceImpl.updateBook(validBook);

        //then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());

        Book capturedBook = bookArgumentCaptor.getValue();

        assertThat(capturedBook).isEqualTo(validBook);
        assertThat(oldModifiedDate).isNotEqualTo(capturedBook.getModifiedAt());
        assertThat(returnedBook).isEqualTo(capturedBook);
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
                .isExactlyInstanceOf(NoResourceFoundException.class)
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
        Book returnedBook = testBookServiceImpl.deleteBook(bookID);

        //then
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).delete(bookCaptor.capture());
        
        Book bookCaptured = bookCaptor.getValue();
        assertThat(bookCaptured).isEqualTo(validBook);
        assertThat(returnedBook).isEqualTo(bookCaptured);
    }

    @Test
    void deleteBookThrowsOperationStoppedException1(){
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
                .isExactlyInstanceOf(NoResourceFoundException.class)
                .hasMessage("No such book found to delete.");

    }

    @Test
    void deleteBookThrowsOperationStoppedException2(){
        //given
        Long bookID = 1L;
        Book validBook = new Book("Garry", "Fiction", 3, 1234);
        User user = new User("Lewis", "Hamilton", "lewis@domain.com");

        validBook.setBookID(bookID);
        validBook.getBorrowers().add(user);
        user.getBorrowedBooks().add(validBook);

        Optional<Book> bookOptional = Optional.of(validBook);
        given(bookRepository
                .findById(validBook.getBookID()))
                .willReturn(bookOptional);

        //when and then
        assertThatThrownBy(() -> testBookServiceImpl.deleteBook(bookID))
                .isExactlyInstanceOf(OperationStoppedException.class)
                .hasMessage("Book already borrowed, book can be deleted when all books are returned.");

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

    @Test
    void borrowBook(){
        //given
        Integer originalBookQty = 3;
        Book book1 = new Book("Garry", "Fiction", originalBookQty, 1234);
        User user1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        user1.setUserID(1L);
        book1.setBookID(1L);

        //user and book both exist.
        Optional<User> optionalUser1 = Optional.of(user1);
        Optional<Book> optionalBook1 = Optional.of(book1);
        given(userService.getUserByID(user1.getUserID())).willReturn(optionalUser1);
        given(bookRepository.findById(book1.getBookID())).willReturn(optionalBook1);

        //book is not already borrowed by user.
        given(bookRepository.isBookBorrowedByUser(user1.getUserID(), book1.getBookID())).willReturn(false);

        //when
        testBookServiceImpl.borrowBook(user1.getUserID(), book1.getBookID());

        //then
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book capturedBook1 = bookCaptor.getValue();
        assertThat(capturedBook1).isEqualTo(book1);
        //check if qty actually decreased.
        assertThat(capturedBook1.getQuantity()).isEqualTo(originalBookQty-1);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).updateUser(userCaptor.capture());
        User capturedUser1 = userCaptor.getValue();
        assertThat(capturedUser1).isEqualTo(user1);
    }

    @Test
    void borrowBookThrowsOperationStoppedException1(){
        //given
        Book book1 = new Book("Garry", "Fiction", 3, 1234);
        User user1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        user1.setUserID(1L);
        book1.setBookID(1L);

        //User doesn't exist, book exists.
        Optional<User> optionalUser1 = Optional.empty();
        Optional<Book> optionalBook1 = Optional.of(book1);
        given(userService.getUserByID(user1.getUserID())).willReturn(optionalUser1);
        given(bookRepository.findById(book1.getBookID())).willReturn(optionalBook1);

        //book is not already borrowed by user.
        given(bookRepository.isBookBorrowedByUser(user1.getUserID(), book1.getBookID())).willReturn(false);

        //when and then
        assertThatThrownBy(() -> testBookServiceImpl.borrowBook(user1.getUserID(), book1.getBookID()) )
                .isExactlyInstanceOf(NoResourceFoundException.class)
                .hasMessage("User or book specified does not exist.");
    }

    @Test
    void borrowBookThrowsOperationStoppedException2(){
        //given
        Book book1 = new Book("Garry", "Fiction", 3, 1234);
        User user1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        user1.setUserID(1L);
        book1.setBookID(1L);

        //User and book both exist.
        Optional<User> optionalUser1 = Optional.of(user1);
        Optional<Book> optionalBook1 = Optional.of(book1);
        given(userService.getUserByID(user1.getUserID())).willReturn(optionalUser1);
        given(bookRepository.findById(book1.getBookID())).willReturn(optionalBook1);

        //book is already borrowed by user.
        given(bookRepository.isBookBorrowedByUser(user1.getUserID(), book1.getBookID())).willReturn(true);

        //when and then
        assertThatThrownBy(() -> testBookServiceImpl.borrowBook(user1.getUserID(), book1.getBookID()) )
                .isExactlyInstanceOf(OperationStoppedException.class)
                .hasMessage("Book already borrowed.");
    }

    @Test
    void borrowBookThrowsOperationStoppedException3(){
        //given
        //NOTE: BOOK QUANTITY IS ZERO HERE
        Book book1 = new Book("Garry", "Fiction", 0, 1234);
        User user1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        user1.setUserID(1L);
        book1.setBookID(1L);

        //User and book both exist.
        Optional<User> optionalUser1 = Optional.of(user1);
        Optional<Book> optionalBook1 = Optional.of(book1);
        given(userService.getUserByID(user1.getUserID())).willReturn(optionalUser1);
        given(bookRepository.findById(book1.getBookID())).willReturn(optionalBook1);

        //book is not already borrowed by user.
        given(bookRepository.isBookBorrowedByUser(user1.getUserID(), book1.getBookID())).willReturn(false);

        //when and then
        assertThatThrownBy(() -> testBookServiceImpl.borrowBook(user1.getUserID(), book1.getBookID()) )
                .isExactlyInstanceOf(OperationStoppedException.class)
                .hasMessage("Book is not available to borrow, quantity is 0.");
    }

    @Test
    void returnBook(){
        //given
        Integer originalBookQty = 3;
        Book book1 = new Book("Garry", "Fiction", originalBookQty, 1234);
        User user1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        user1.setUserID(1L);
        book1.setBookID(1L);

        //user and book both exist.
        Optional<User> optionalUser1 = Optional.of(user1);
        Optional<Book> optionalBook1 = Optional.of(book1);
        given(userService.getUserByID(user1.getUserID())).willReturn(optionalUser1);
        given(bookRepository.findById(book1.getBookID())).willReturn(optionalBook1);

        //book is already borrowed by user.
        given(bookRepository.isBookBorrowedByUser(user1.getUserID(), book1.getBookID())).willReturn(true);

        //when
        testBookServiceImpl.returnBook(user1.getUserID(), book1.getBookID());

        //then
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book capturedBook1 = bookCaptor.getValue();
        assertThat(capturedBook1).isEqualTo(book1);

        //check if qty actually increased.
        assertThat(capturedBook1.getQuantity()).isEqualTo(originalBookQty+1);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).updateUser(userCaptor.capture());
        User capturedUser1 = userCaptor.getValue();
        assertThat(capturedUser1).isEqualTo(user1);
    }

    @Test
    void returnBookThrowsOperationStoppedException1(){
        //given
        Book book1 = new Book("Garry", "Fiction", 3, 1234);
        User user1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        user1.setUserID(1L);
        book1.setBookID(1L);

        //user and book both exist.
        Optional<User> optionalUser1 = Optional.of(user1);
        Optional<Book> optionalBook1 = Optional.empty();
        given(userService.getUserByID(user1.getUserID())).willReturn(optionalUser1);
        given(bookRepository.findById(book1.getBookID())).willReturn(optionalBook1);

        //book is already borrowed by user.
        given(bookRepository.isBookBorrowedByUser(user1.getUserID(), book1.getBookID())).willReturn(true);

        //when and then
        assertThatThrownBy(() -> testBookServiceImpl.returnBook(user1.getUserID(), book1.getBookID()))
                .isExactlyInstanceOf(NoResourceFoundException.class)
                .hasMessage("User or book being returned does not exist.");
    }

    @Test
    void returnBookThrowsOperationStoppedException2(){
        //given
        Book book1 = new Book("Garry", "Fiction", 3, 1234);
        User user1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        user1.setUserID(1L);
        book1.setBookID(1L);

        //user and book both exist.
        Optional<User> optionalUser1 = Optional.of(user1);
        Optional<Book> optionalBook1 = Optional.of(book1);
        given(userService.getUserByID(user1.getUserID())).willReturn(optionalUser1);
        given(bookRepository.findById(book1.getBookID())).willReturn(optionalBook1);

        //book is already borrowed by user.
        given(bookRepository.isBookBorrowedByUser(user1.getUserID(), book1.getBookID())).willReturn(false);

        //when and then
        assertThatThrownBy(() -> testBookServiceImpl.returnBook(user1.getUserID(), book1.getBookID()))
                .isExactlyInstanceOf(OperationStoppedException.class)
                .hasMessage("You can only return borrowed books.");
    }
}