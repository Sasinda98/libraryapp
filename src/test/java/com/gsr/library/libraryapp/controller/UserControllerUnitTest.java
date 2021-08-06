package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.dto.ListBookDto;
import com.gsr.library.libraryapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

    @Mock
    private UserService testUserService;
    private UserController testUserController;
    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        testUserController = new UserController(testUserService, modelMapper);
    }

    @Test
    void getUsers() {
        testUserController.getUsers();
        verify(testUserService).getUsers();
    }

    @Test
    void getBorrowersForABook() {
        //given
        Long userID = 1L;
        Long bookID = 2L;
        Book b1 = new Book("Garry", "Fiction", 3, 1234);
        b1.setBookID(bookID);

        ArrayList<Book> bookArrayList = new ArrayList<>();
        bookArrayList.add(b1);

        given(testUserService.getBooksBorrowedByUserID(userID))
                .willReturn(bookArrayList);
        //when
        ListBookDto actualReturn = testUserController.getBooksBorrowedByAUser(userID);
        //then
        ArgumentCaptor<Long> userIDCaptor = ArgumentCaptor.forClass(Long.class);
        verify(testUserService).getBooksBorrowedByUserID(userIDCaptor.capture());

        Long capturedUserID = userIDCaptor.getValue();

        assertThat(capturedUserID).isEqualTo(userID);
        assertThat(actualReturn.getNoOfBooks()).isEqualTo(bookArrayList.size());
        assertThat(actualReturn.getBooks().get(0).getBookID()).isEqualTo(bookID);
    }
}