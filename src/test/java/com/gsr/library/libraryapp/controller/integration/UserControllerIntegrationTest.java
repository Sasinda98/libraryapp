package com.gsr.library.libraryapp.controller.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsr.library.libraryapp.controller.UserController;
import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.domain.dto.ListBookDto;
import com.gsr.library.libraryapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    @Test
    void getBooksBorrowedByAUser() throws Exception {
        //given
        Book b1 = new Book("Garry", "Fiction", 3, 1234);
        b1.setBookID(3L);
        Book b2 = new Book("Bravo Two Zero", "Non Fiction", 3, 2345);
        b2.setBookID(4L);

        List<Book> bookList = new ArrayList<>();
        bookList.add(b1);
        bookList.add(b2);
        List<BookDto> bookDtoList = bookList.stream().map(book -> modelMapper.map(book, BookDto.class)).collect(Collectors.toList());
        ListBookDto responseExpected = new ListBookDto(bookDtoList.size(), bookDtoList);

        when(userService.getBooksBorrowedByUserID(1L)).thenReturn(bookList);

        //when and then
        MvcResult result = mockMvc.perform(
            get("/users/{user_id}/books", 1)
        )
        .andExpect(status().isOk())
        .andReturn();

        ListBookDto response = objectMapper.readValue(result.getResponse().getContentAsString(), ListBookDto.class);

        assertThat(response).isEqualTo(responseExpected);
    }
}