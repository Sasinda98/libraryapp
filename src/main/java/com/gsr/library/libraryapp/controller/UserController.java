package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.domain.dto.ListBookDto;
import com.gsr.library.libraryapp.domain.dto.ListUserDto;
import com.gsr.library.libraryapp.services.UserService;
import com.gsr.library.libraryapp.services.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userServiceImpl;

    private final ModelMapper modelMapper;

    public UserController(UserService userServiceImpl, ModelMapper modelMapper) {
        this.userServiceImpl = userServiceImpl;
        this.modelMapper = modelMapper;
    }

    @ApiIgnore
    @GetMapping
    public List<User> getUsers(){
        return userServiceImpl.getUsers();
    }

    @ApiOperation(value = "Gets a list of books borrowed by a specific user.")
    @GetMapping("/{user_id}/books")
    public ListBookDto getBooksBorrowedByAUser(@PathVariable(name = "user_id") Long userID){
        List<Book> books = userServiceImpl.getBooksBorrowedByUserID(userID);
        List<BookDto> bookDtos = books.stream().map(book -> modelMapper.map(book, BookDto.class)).collect(Collectors.toList());
        return new ListBookDto(bookDtos.size(), bookDtos);
    }
}

