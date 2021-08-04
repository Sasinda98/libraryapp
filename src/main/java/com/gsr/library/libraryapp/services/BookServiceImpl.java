package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    @Override
    public Boolean bookExistsByID(Long bookID) {
        return null;
    }

    @Override
    public Boolean isBookBorrowedByUser(Long userID, Long bookID) {
        return null;
    }

    public List<Book> searchForBookByTitle(String title){
        return (List<Book>) bookRepository.searchForBookByTitle(title);
    }

    @Override
    public List<User> getBorrowersForABookByBookID(Long bookID) {
        return null;
    }

}
