package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.repositories.BookRepository;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class BookController {
    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks() {
        return (List<Book>) bookRepository.findAll();
    }
}
