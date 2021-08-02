package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.repositories.BookRepository;
import com.gsr.library.libraryapp.services.BookService;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public List<Book> getBooks() {
        return (List<Book>) bookService.getBooks();
    }
}
