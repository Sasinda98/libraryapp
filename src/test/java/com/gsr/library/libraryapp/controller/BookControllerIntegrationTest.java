package com.gsr.library.libraryapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.domain.dto.APISuccessResponseDto;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.domain.dto.ListUserDto;
import com.gsr.library.libraryapp.domain.dto.UserDto;
import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.exceptions.exceptiontemplates.APIExceptionTemplate;
import com.gsr.library.libraryapp.repositories.BookRepository;
import com.gsr.library.libraryapp.services.BookService;
import com.gsr.library.libraryapp.services.BookServiceImpl;
import com.gsr.library.libraryapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.ui.ModelMap;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(BookController.class)
class BookControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean BookService bookService;

    private final ModelMapper modelMapper = new ModelMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private String getJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void getBorrowersForABookNoBorrowers() throws Exception {
        mockMvc.perform(
            get("/books/{book_id}/borrowers", 1)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
                .andDo(print())
        .andExpect(jsonPath("$.number_of_users").value(0))
        .andExpect(jsonPath("$.users").value(new ArrayList<User>()));
    }

    @Test
    void getBorrowersForABookBorrowersAvail() throws Exception {
        //given
        User u1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        u1.setUserID(1L);
        User u2 = new User("John", "Doe", "john@domain.com");
        u2.setUserID(2L);
        List<User> usersList = new ArrayList<>();
        usersList.add(u1);
        usersList.add(u2);

        given(bookService.getBorrowersForABookByBookID(1L))
                .willReturn(usersList);

        List<UserDto> userDtoList = usersList
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        ListUserDto expectedResponse = new ListUserDto(userDtoList.size(), userDtoList);

        //when and then
        MvcResult result = mockMvc.perform(
                get("/books/{book_id}/borrowers", 1)
        )
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.number_of_users").value(usersList.size()))
            .andDo(print())
            .andReturn();
        //deserialize response
        ListUserDto responseListUserDto = objectMapper.readValue(result.getResponse().getContentAsString(), ListUserDto.class);

        assertThat(responseListUserDto).isEqualTo(expectedResponse);
    }

    @Test
    void deleteABook() throws Exception {
        //given
        Long bookID = 1L;
        Book book = new Book("Garry", "Fiction", 3, 1234);
        book.setBookID(bookID);

        when(bookService.deleteBook(bookID)).thenReturn(book);

        BookDto expectedResponse = modelMapper.map(book, BookDto.class);

        //when and then
        MvcResult mvcResult = mockMvc.perform(
            delete("/books/{book_id}", bookID)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        BookDto responseBook = objectMapper.readValue(responseString, BookDto.class);

        assertThat(responseBook).isEqualTo(expectedResponse);
    }
    
    @Test
    void borrowBook() throws Exception {
        //given
        HashMap<String, Object> requestContent = new HashMap<>();
        requestContent.put("book_id", 1L);
        requestContent.put("user_id", 2L);

        APISuccessResponseDto expectedResponse = new APISuccessResponseDto("Book successfully borrowed.");

        //when and then
        MvcResult result = mockMvc.perform(post("/books/borrow-info")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getJsonString(requestContent))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

        verify(bookService).borrowBook(2L, 1L);

        APISuccessResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), APISuccessResponseDto.class);
        assertThat(responseDto).isEqualTo(expectedResponse);
    }

    @Test
    void returnBook() throws Exception {
        //given
        HashMap<String, Object> requestContent = new HashMap<>();
        requestContent.put("book_id", 1L);
        requestContent.put("user_id", 2L);

        APISuccessResponseDto expectedResponse = new APISuccessResponseDto("Book successfully returned.");

        //when and then
        MvcResult result = mockMvc.perform(post("/books/return-info")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getJsonString(requestContent))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

        verify(bookService).returnBook(2L, 1L);

        APISuccessResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), APISuccessResponseDto.class);
        assertThat(responseDto).isEqualTo(expectedResponse);
    }

    @Test
    void updateBook() throws Exception {
        //given
        HashMap<String, Object> requestContent = new HashMap<>();
        requestContent.put("book_id", 1L);
        requestContent.put("title", "Sample title.");

        APISuccessResponseDto responseExpected = new APISuccessResponseDto("Book updated successfully");

        //when and then
        MvcResult result = mockMvc.perform(
                put("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonString(requestContent))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

        APISuccessResponseDto response = objectMapper.readValue(result.getResponse().getContentAsString(), APISuccessResponseDto.class);
        assertThat(response).isEqualTo(responseExpected);
    }

    @Test
    void checkAPIResponseOnNoResourceFoundException() throws Exception {
        //given
        HashMap<String, Object> requestContent = new HashMap<>();
        requestContent.put("book_id", 1L);
        requestContent.put("title", "Sample title.");

        Book b1 = new Book();
        b1.setBookID(1L);
        b1.setTitle("Sample title.");

        //FORCE throw NoResourceFoundException.
        when(bookService.updateBook(b1)).thenThrow(new NoResourceFoundException("Throw NoResourceFoundException and check response."));

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        APIExceptionTemplate responseExpected = new APIExceptionTemplate( "Throw NoResourceFoundException and check response.", httpStatus.getReasonPhrase(), httpStatus.value(), new Date());

        //when and then
        //hit the endpoint where this is thrown and get response.
        MvcResult result = mockMvc.perform(put("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonString(requestContent))
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        APIExceptionTemplate response = objectMapper.readValue(result.getResponse().getContentAsString(), APIExceptionTemplate.class);
        assertThat(response).isEqualTo(responseExpected);
    }

    @Test
    void checkAPIResponseOnValidationException() throws Exception {
        //given
        HashMap<String, Object> requestContent = new HashMap<>();
        requestContent.put("book_id", 1L);
        requestContent.put("title", "Sample title.");

        Book b1 = new Book();
        b1.setBookID(1L);
        b1.setTitle("Sample title.");

        //FORCE throw ValidationException
        when(bookService.updateBook(b1)).thenThrow(new ValidationException("Throw ValidationException and check response."));

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        APIExceptionTemplate responseExpected = new APIExceptionTemplate( "Throw ValidationException and check response.", httpStatus.getReasonPhrase(), httpStatus.value(), new Date());

        //when and then
        //hit the endpoint where this is thrown and get response.
        MvcResult result = mockMvc.perform(put("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonString(requestContent))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        APIExceptionTemplate response = objectMapper.readValue(result.getResponse().getContentAsString(), APIExceptionTemplate.class);
        assertThat(response).isEqualTo(responseExpected);
    }

    @Test
    void checkAPIResponseOnOperationStoppedException() throws Exception {
        //given
        HashMap<String, Object> requestContent = new HashMap<>();
        requestContent.put("book_id", 1L);
        requestContent.put("user_id", 2L);

        //FORCE throw OperationStoppedException
        when(bookService.borrowBook(2L, 1L)).thenThrow(new OperationStoppedException("Throw OperationStoppedException and check response."));

        HttpStatus httpStatus = HttpStatus.CONFLICT;
        APIExceptionTemplate responseExpected = new APIExceptionTemplate( "Throw OperationStoppedException and check response.", httpStatus.getReasonPhrase(), httpStatus.value(), new Date());

        //when and then
        //hit the endpoint where this is thrown and get response.
        MvcResult result = mockMvc.perform(post("/books/borrow-info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getJsonString(requestContent))
        )
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn();

        APIExceptionTemplate response = objectMapper.readValue(result.getResponse().getContentAsString(), APIExceptionTemplate.class);
        assertThat(response).isEqualTo(responseExpected);
    }

}