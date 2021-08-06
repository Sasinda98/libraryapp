package com.gsr.library.libraryapp.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.domain.dto.ListUserDto;
import com.gsr.library.libraryapp.domain.dto.UserDto;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.services.BookServiceImpl;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/books")
public class BookController {
    private final BookServiceImpl bookServiceImpl;
    private final ModelMapper modelMapper;

    public BookController(BookServiceImpl bookServiceImpl, ModelMapper modelMapper) {
        this.bookServiceImpl = bookServiceImpl;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<Book> getBooks() {
        return bookServiceImpl.getAllBooks();
    }

    @GetMapping("/{book_id}/borrowers")
    public ListUserDto getBorrowersForABook(@PathVariable(name = "book_id") Long book_id){
        List<User> users = bookServiceImpl.getBorrowersForABookByBookID(book_id);
        List<UserDto> userDtos = users.stream().map(user1 -> modelMapper.map(user1, UserDto.class)).collect(Collectors.toList());
        return new ListUserDto(userDtos.size(), userDtos);
    }

    @DeleteMapping(value = "/{book_id}")
    public BookDto deleteABook(@PathVariable("book_id") Long bookID){
        Book deletedBook = bookServiceImpl.deleteBook(bookID);
        return modelMapper.map(deletedBook, BookDto.class);
    }

    @PostMapping(value = "/borrow-info")
    public Map<String, Object> borrowBook(@RequestBody Map<String, Object> payload){
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
        HashMap<String, Object> success = new HashMap<>();
        success.put("message", "Book successfully borrowed.");
        return success;
    }

    @PostMapping(value = "/return-info")
    public Map<String, Object> returnBook (@RequestBody Map<String, Object> payload){
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
        HashMap<String, Object> success = new HashMap<>();
        success.put("message", "Book successfully returned.");
        return success;
    }

    @PutMapping
    public Map<String, Object> updateBook (@RequestBody Map<String, Object> payload){
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
        HashMap<String, Object> success = new HashMap<>();
        success.put("message", "UPDATE NOT IMPLEMENTED");
        return success;
    }
}
