package com.gsr.library.libraryapp.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.domain.dto.APISuccessResponseDto;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.domain.dto.ListUserDto;
import com.gsr.library.libraryapp.domain.dto.UserDto;
import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.exceptions.exceptiontemplates.APIExceptionTemplate;
import com.gsr.library.libraryapp.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/books")
public class BookController {
    private final BookService bookServiceImpl;
    private final ModelMapper modelMapper;

    public BookController(BookService bookServiceImpl, ModelMapper modelMapper) {
        this.bookServiceImpl = bookServiceImpl;
        this.modelMapper = modelMapper;
    }

    @ApiIgnore
    @GetMapping
    public List<Book> getBooks() {
        return bookServiceImpl.getAllBooks();
    }

    @ApiOperation(value = "Gets a list of users who has borrrowed a specific book.")
    @GetMapping("/{book_id}/borrowers")
    public ListUserDto getBorrowersForABook(@PathVariable(name = "book_id") Long book_id){
        List<User> users = bookServiceImpl.getBorrowersForABookByBookID(book_id);
        List<UserDto> userDtos = users.stream().map(user1 -> modelMapper.map(user1, UserDto.class)).collect(Collectors.toList());
        return new ListUserDto(userDtos.size(), userDtos);
    }

    @ApiOperation(value = "Deletes a book")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No such book to delete.", response = APIExceptionTemplate.class),
            @ApiResponse(code = 409, message = "Book already borrowed, cannot delete until all books are returned.", response = APIExceptionTemplate.class)
    })
    @DeleteMapping(value = "/{book_id}")
    public BookDto deleteABook(@PathVariable("book_id") Long bookID){
        Book deletedBook = bookServiceImpl.deleteBook(bookID);
        return modelMapper.map(deletedBook, BookDto.class);
    }

    @ApiOperation(value = "Allows a user to borrow a book.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No such book or user.", response = APIExceptionTemplate.class),
            @ApiResponse(code = 400, message = "Bad request.", response = APIExceptionTemplate.class),
            @ApiResponse(code = 409, message = "Book is not available to borrow (qty = 0) or is already borrowed.", response = APIExceptionTemplate.class),
    })
    @PostMapping(value = "/borrow-info")
    public APISuccessResponseDto borrowBook(@RequestBody Map<String, Object> payload){
        try {
            Long userID = ((Number) payload.get("user_id")).longValue();
            Long bookID = ((Number) payload.get("book_id")).longValue();
            bookServiceImpl.borrowBook(userID, bookID);
        }
        catch (ClassCastException | NullPointerException ex){
            //When user_id book_id in req body is not in type we expect.
            //Or the keys don't exist at all.
            throw new ValidationException("Bad request received.");
        }

        return new APISuccessResponseDto("Book successfully borrowed.");
    }

    @ApiOperation(value = "Allows a user to return a book.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No such book or user.", response = APIExceptionTemplate.class),
            @ApiResponse(code = 400, message = "Bad request.", response = APIExceptionTemplate.class),
            @ApiResponse(code = 409, message = "Only borrowed books can be returned.", response = APIExceptionTemplate.class),
    })
    @PostMapping(value = "/return-info")
    public APISuccessResponseDto returnBook (@RequestBody Map<String, Object> payload){
        try {
            Long userID = ((Number) payload.get("user_id")).longValue();
            Long bookID = ((Number) payload.get("book_id")).longValue();
            bookServiceImpl.returnBook(userID, bookID);
        }
        catch (ClassCastException | NullPointerException ex){
            //When user_id book_id in req body is not in type we expect.
            //Or the keys don't exist at all.
            throw new ValidationException("Bad request received.");
        }
        return new APISuccessResponseDto("Book successfully returned.");
    }

    @ApiOperation(value = "Allows a user to update details of a book.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No such book to update.", response = APIExceptionTemplate.class),
            @ApiResponse(code = 400, message = "Bad request.", response = APIExceptionTemplate.class),
    })
    @PutMapping
    public APISuccessResponseDto updateBook (@RequestBody Map<String, Object> payload){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            BookDto bookDto = objectMapper.convertValue(payload, BookDto.class);

            //ToDo: Check why these two disagree.
            System.out.println("DTO from ObjectMapper == " + bookDto);
            System.out.println("DTO from modelMapper == " + modelMapper.map(Map.class, BookDto.class));

            Book book = modelMapper.map(bookDto, Book.class);

            System.out.println("Actual Book = " + book);

            if(book.getBookID() == null){
                throw new ValidationException("Specify book by its id.");
            }
            bookServiceImpl.updateBook(book);

        }
        catch (IllegalArgumentException ex){
            //Arguments specified is outside API spec.
            throw new ValidationException("Bad request received.");
        }
        return new APISuccessResponseDto("Book updated successfully");
    }
}
