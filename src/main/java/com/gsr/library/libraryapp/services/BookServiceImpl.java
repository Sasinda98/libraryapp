package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return bookRepository.bookExistsByID(bookID);
    }

    @Override
    public Boolean isBookBorrowedByUser(Long userID, Long bookID) {
        return bookRepository.isBookBorrowedByUser(userID,bookID);
    }

    public List<Book> searchForBookByTitle(String title){
        return bookRepository.searchForBookByTitle(title);
    }

    @Override
    public List<User> getBorrowersForABookByBookID(Long bookID) {
        return bookRepository.getBorrowersForABookByBookID(bookID);
    }

    @Override
    public void updateBook(Book book) throws ValidationException, OperationStoppedException {
        Optional<Book> bookOptional = bookRepository.findById(book.getBookID());

        if (!bookOptional.isPresent()){
            throw new OperationStoppedException("No such book found to update.");
        }

        //Implement validation here.

        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookID) throws OperationStoppedException {
        Optional<Book> bookOptional = bookRepository.findById(bookID);

        if (!bookOptional.isPresent()){
            throw new OperationStoppedException("No such book found to delete.");
        }

        bookRepository.delete(bookOptional.get());
    }
}
