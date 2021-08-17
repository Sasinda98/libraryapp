package com.gsr.library.libraryapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.domain.dto.APISuccessResponseDto;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.domain.dto.ListUserDto;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerUnitTest {
    @Mock private BookService testBookService;
    private BookController testBookController;
    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        testBookController = new BookController(testBookService, modelMapper);
    }

    @Test
    void getBooks(){
        testBookController.getBooks();
        verify(testBookService).getAllBooks();
    }

    @Test
    void getBorrowersForABook() {
        //given
        Long bookID = 1L;
        Long userID = 2L;
        User User = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        User.setUserID(userID);

        ArrayList<User> UserArrayList = new ArrayList<>();
        UserArrayList.add(User);

        given(testBookService.getBorrowersForABookByBookID(bookID))
                .willReturn(UserArrayList);

        //when
        ListUserDto actualReturn = testBookController.getBorrowersForABook(bookID);

        //then
        ArgumentCaptor<Long> bookIDCaptor = ArgumentCaptor.forClass(Long.class);
        verify(testBookService).getBorrowersForABookByBookID(bookIDCaptor.capture());

        Long bookIDCaptured = bookIDCaptor.getValue();

        assertThat(bookIDCaptured).isEqualTo(bookID);
        assertThat(actualReturn.getNumberOfUsers()).isEqualTo(UserArrayList.size());
        assertThat(actualReturn
                .getUsers()
                .get(0)
                .getUserID()
        ).isEqualTo(userID);
    }

    @Test
    void deleteABook() {
        //given
        Long bookID = 1L;
        Book b1 = new Book("Garry", "Fiction", 3, 1234);
        b1.setBookID(bookID);

        given(testBookService.deleteBook(bookID)).willReturn(b1);

        //when
        BookDto returnedBookDto = testBookController.deleteABook(bookID);
        Book returnedBook = modelMapper.map(returnedBookDto, Book.class);

        //then
        ArgumentCaptor<Long> bookIDCaptor = ArgumentCaptor.forClass(Long.class);
        verify(testBookService).deleteBook(bookIDCaptor.capture());
        Long bookIDCaptured = bookIDCaptor.getValue();
        assertThat(bookIDCaptured).isEqualTo(bookID);

        assertThat(returnedBook).isEqualTo(b1);
    }

    @Test
    void borrowBook() {
        //given
        String userIDKey = "user_id";
        String bookIDKey = "book_id";
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put(userIDKey, 1L);
        requestBody.put(bookIDKey, 2L);

        //when
        APISuccessResponseDto successResponseDto = testBookController.borrowBook(requestBody);

        //then
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(testBookService).borrowBook(idCaptor.capture(), idCaptor.capture());

        Long capturedUserID = idCaptor.getAllValues().get(0);
        Long capturedBookID = idCaptor.getAllValues().get(1);
        assertThat(capturedUserID).isEqualTo(requestBody.get(userIDKey));
        assertThat(capturedBookID).isEqualTo(requestBody.get(bookIDKey));

        assertThat(successResponseDto.getMessage()).isEqualTo("Book successfully borrowed.");
    }

    @Test
    void borrowBookThrowsValidationException1() {
        //given
        String userIDKey = "user_id";
        String bookIDKey = "book_id";
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put(userIDKey, "sdasdas");
        requestBody.put(bookIDKey, "sdasdadg");

        //when and then
        assertThatThrownBy(()->testBookController.borrowBook(requestBody))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage("Bad request received.");
    }

    @Test
    void returnBook() {
        //given
        String userIDKey = "user_id";
        String bookIDKey = "book_id";
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put(userIDKey, 1L);
        requestBody.put(bookIDKey, 2L);

        //when
        APISuccessResponseDto successResponseDto = testBookController.returnBook(requestBody);

        //then
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(testBookService).returnBook(idCaptor.capture(), idCaptor.capture());

        Long capturedUserID = idCaptor.getAllValues().get(0);
        Long capturedBookID = idCaptor.getAllValues().get(1);
        assertThat(capturedUserID).isEqualTo(requestBody.get(userIDKey));
        assertThat(capturedBookID).isEqualTo(requestBody.get(bookIDKey));

        assertThat(successResponseDto.getMessage()).isEqualTo("Book successfully returned.");
    }

    @Test
    void returnBookThrowsValidationException1() {
        //given
        String userIDKey = "user_id";
        String bookIDKey = "book_id";
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put(userIDKey, "sdasdas");
        requestBody.put(bookIDKey, "sdasdadg");

        //when and then
        assertThatThrownBy(()->testBookController.returnBook(requestBody))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage("Bad request received.");
    }

    @Test
    void updateBook() {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        Long bookID = 1L;
        Book bookObjectRepresentingAChange = new Book(null, "Fiction", null, null);
        bookObjectRepresentingAChange.setBookID(bookID);

        BookDto changeDto = modelMapper.map(bookObjectRepresentingAChange, BookDto.class);

        HashMap<String, Object> requestBody = objectMapper.convertValue(changeDto, new TypeReference<HashMap<String, Object>>(){});

        //when
        APISuccessResponseDto successResponseDto = testBookController.updateBook(requestBody);

        //then
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(testBookService).updateBook(bookCaptor.capture());

        Book capturedBook = bookCaptor.getValue();
        assertThat(capturedBook).isEqualTo(bookObjectRepresentingAChange);
        assertThat(successResponseDto.getMessage()).isEqualTo("Book updated successfully");
    }

    @Test
    void updateBookThrowsValidationException1() {
        //given
        HashMap<String, Object> requestBodyOutOfAPISpec = new HashMap<>();
        requestBodyOutOfAPISpec.put("title", "dasdas");
        requestBodyOutOfAPISpec.put("r34asde3", 23);
        //when and then
        assertThatThrownBy(()->testBookController.updateBook(requestBodyOutOfAPISpec))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage("Bad request received.");
    }

    @Test
    void updateBookThrowsValidationException2() {
        //given
        HashMap<String, Object> requestBodyOutOfAPISpec = new HashMap<>();
        //Not specifying book_id.
        requestBodyOutOfAPISpec.put("title", "dasdas");
        requestBodyOutOfAPISpec.put("category", "category");
        //when and then
        assertThatThrownBy(()->testBookController.updateBook(requestBodyOutOfAPISpec))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage("Specify book by its id.");
    }
}