package com.gsr.library.libraryapp.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.domain.dto.APISuccessResponseDto;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.domain.dto.ListUserDto;
import com.gsr.library.libraryapp.domain.dto.UserDto;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.exceptions.exceptiontemplates.APIExceptionTemplate;
import com.gsr.library.libraryapp.services.BookService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @Hidden
    @GetMapping
    @PreAuthorize("#oauth2.hasScope('READ')")
    public List<Book> getBooks() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getCredentials());

        return bookServiceImpl.getAllBooks();
    }

    @Operation(summary = "Gets a list of users who has borrowed a specific book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lists the borrowers of the book.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ListUserDto.class)) }),
    })
    @GetMapping("/{book_id}/borrowers")
    @PreAuthorize("hasAnyRole('ROLE_librarian', 'ROLE_teacher') || #oauth2.hasScope('READ')")
    public ListUserDto getBorrowersForABook(@Parameter(description = "The book id of the book to list borrowers. ", required = true) @PathVariable(name = "book_id") Long book_id){
        List<User> Users = bookServiceImpl.getBorrowersForABookByBookID(book_id);
        List<UserDto> userDtos = Users.stream().map(user1 -> modelMapper.map(user1, UserDto.class)).collect(Collectors.toList());
        return new ListUserDto(userDtos.size(), userDtos);
    }

    @Operation(summary = "Deletes a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDto.class)) }),
            @ApiResponse(responseCode = "404", description = "No such book to delete.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionTemplate.class)) }),
            @ApiResponse(responseCode = "409", description = "Book already borrowed, cannot delete until all books are returned.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionTemplate.class)) }),

    })
    @DeleteMapping(value = "/{book_id}")
    @PreAuthorize("hasRole('ROLE_librarian') || #oauth2.hasScope('WRITE')")
    public BookDto deleteABook(@PathVariable("book_id") Long bookID){
        Book deletedBook = bookServiceImpl.deleteBook(bookID);
        return modelMapper.map(deletedBook, BookDto.class);
    }

    @Operation(summary = "Allows a user to borrow a book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book borrowed successfully.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APISuccessResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "No such book or user.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionTemplate.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionTemplate.class)) }),
            @ApiResponse(responseCode = "409", description = "Book is not available to borrow (qty = 0) or is already borrowed.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionTemplate.class)) }),
    })
    @PostMapping(value = "/borrow-info")
    @PreAuthorize("hasAuthority('borrow_book') || #oauth2.hasScope('WRITE')")
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

    @Operation(summary = "Allows a user to return a book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APISuccessResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "No such book or user.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionTemplate.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionTemplate.class)) }),
            @ApiResponse(responseCode = "409", description = "Only borrowed books can be returned.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionTemplate.class)) }),
    })
    @PostMapping(value = "/return-info")
    @PreAuthorize("hasAuthority('return_book') || #oauth2.hasScope('WRITE')")
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

    @Operation(summary = "Allows a user to update details of a book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APISuccessResponseDto.class)) }),
            @ApiResponse(responseCode = "404", description = "No such book to update.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionTemplate.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = APIExceptionTemplate.class)) }),
    })
    @PutMapping
    @PreAuthorize("hasRole('ROLE_librarian') || #oauth2.hasScope('WRITE')")
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

            if(book.getId() == null){
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
