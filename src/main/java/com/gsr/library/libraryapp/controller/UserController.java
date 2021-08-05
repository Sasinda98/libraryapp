package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.domain.dto.ListBookDto;
import com.gsr.library.libraryapp.domain.dto.ListUserDto;
import com.gsr.library.libraryapp.services.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    private final ModelMapper modelMapper;

    public UserController(UserServiceImpl userServiceImpl, ModelMapper modelMapper) {
        this.userServiceImpl = userServiceImpl;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<User> getUsers(){
        return userServiceImpl.getUsers();
    }
    
    @GetMapping("/{user_id}/books")
    public ListBookDto getBorrowersForABook(@PathVariable(name = "user_id") Long userID){
        List<Book> books = userServiceImpl.getBooksBorrowedByUserID(userID);
        List<BookDto> bookDtos = books.stream().map(book -> modelMapper.map(book, BookDto.class)).collect(Collectors.toList());
        return new ListBookDto(bookDtos.size(), bookDtos);
    }
}

