package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.domain.dto.ListUserDto;
import com.gsr.library.libraryapp.domain.dto.UserDto;
import com.gsr.library.libraryapp.services.BookServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

}
