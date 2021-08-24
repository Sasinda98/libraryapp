package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.domain.dto.ListBookDto;
import com.gsr.library.libraryapp.domain.dto.ListUserDto;
import com.gsr.library.libraryapp.services.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @Hidden
    @GetMapping
    public List<User> getUsers(){
        return userServiceImpl.getUsers();
    }


    @Operation(summary = "Gets a list of books borrowed by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lists the books borrowed by user.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ListBookDto.class)) }),
    })
    @GetMapping("/{user_id}/books")
    @PreAuthorize("hasAnyRole('ROLE_librarian', 'ROLE_teacher') || #oauth2.hasScope('READ')")
    public ListBookDto getBooksBorrowedByAUser(@PathVariable(name = "user_id") Long userID){
        List<Book> books = userServiceImpl.getBooksBorrowedByUserID(userID);
        List<BookDto> bookDtos = books.stream().map(book -> modelMapper.map(book, BookDto.class)).collect(Collectors.toList());
        return new ListBookDto(bookDtos.size(), bookDtos);
    }
}

