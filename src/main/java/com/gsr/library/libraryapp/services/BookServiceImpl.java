package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.domain.dto.BookDto;
import com.gsr.library.libraryapp.exceptions.NoResourceFoundException;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.micellaneous.Validator;
import com.gsr.library.libraryapp.repositories.BookRepository;
import jdk.internal.module.IllegalAccessLogger;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final UserService userService;

    private final ModelMapper modelMapper;

    public BookServiceImpl(BookRepository bookRepository, UserService userService, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> bookExistsByID(Long bookID) {
        return bookRepository.findById(bookID);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isBookBorrowedByUser(Long userID, Long bookID) {
        return bookRepository.isBookBorrowedByUser(userID,bookID);
    }

    @Transactional(readOnly = true)
    public List<Book> searchForBookByTitle(String title){
        return bookRepository.searchForBookByTitle(title);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getBorrowersForABookByBookID(Long bookID) {
        return bookRepository.getBorrowersForABookByBookID(bookID);
    }

    @Override
    @Transactional
    public Book updateBook(Book book) throws ValidationException, OperationStoppedException {
        Optional<Book> bookOptional = bookRepository.findById(book.getBookID());

        if (!bookOptional.isPresent()){
            throw new NoResourceFoundException("No such book found to update.");
        }

        Book bookRecord = bookOptional.get();
        //Apply the not null fields of book to bookRecord, thus bringing the record up to date.
        modelMapper.typeMap(Book.class, Book.class).setPropertyCondition(Conditions.isNotNull()).map(book, bookRecord);
        //Implement validation here.
        if(!Validator.getInstance().isBookValid(bookRecord)){
            throw new ValidationException("Invalid book details.");
        }

        //change modified date.
        bookRecord.setModifiedAt(new Date());
        bookRepository.save(bookRecord);
        return bookRecord;
    }

    @Override
    @Transactional
    public Book deleteBook(Long bookID) throws OperationStoppedException {
        Optional<Book> bookOptional = bookRepository.findById(bookID);

        if (!bookOptional.isPresent()){
            throw new NoResourceFoundException("No such book found to delete.");
        }

        Boolean isAlreadyBorrowed = !bookOptional.get().getBorrowers().isEmpty();
        if(isAlreadyBorrowed){
            throw new OperationStoppedException("Book already borrowed, book can be deleted when all books are returned.");
        }

        bookRepository.delete(bookOptional.get());
        return bookOptional.get();
    }

    @Override
    @Transactional
    public Boolean borrowBook(Long userID, Long bookID) throws OperationStoppedException {
        Optional<User> optionalUser = userService.getUserByID(userID);
        Optional<Book> optionalBook = bookRepository.findById(bookID);
        Boolean isBorrowed = bookRepository.isBookBorrowedByUser(userID, bookID);

        if(!optionalUser.isPresent() || !optionalBook.isPresent())
            throw new NoResourceFoundException("User or book specified does not exist.");
        if(isBorrowed)
            throw new OperationStoppedException("Book already borrowed.");

        Boolean isBookAvailForBorrow = optionalBook.get().getQuantity() > 0;
        if(!isBookAvailForBorrow)
            throw new OperationStoppedException("Book is not available to borrow, quantity is 0.");

        //Green light to borrow.
        User user = optionalUser.get();
        Book book = optionalBook.get();

        book.setQuantity(book.getQuantity() - 1);
        user.getBorrowedBooks().add(book);
        book.getBorrowers().add(user);

        bookRepository.save(book);
        userService.updateUser(user);
        return true;
    }

    @Override
    @Transactional
    public Boolean returnBook(Long userID, Long bookID) throws OperationStoppedException {
        Optional<User> optionalUser = userService.getUserByID(userID);
        Optional<Book> optionalBook = bookRepository.findById(bookID);
        Boolean isBorrowed = bookRepository.isBookBorrowedByUser(userID, bookID);

        if(!isBorrowed)
            throw new OperationStoppedException("You can only return borrowed books.");
        if(!optionalUser.isPresent() || !optionalBook.isPresent())
            throw new NoResourceFoundException("User or book being returned does not exist.");

        //Green light to return.
        User user = optionalUser.get();
        Book book = optionalBook.get();

        book.setQuantity(book.getQuantity() + 1);
        user.getBorrowedBooks().remove(book);
        book.getBorrowers().remove(user);

        bookRepository.save(book);
        userService.updateUser(user);
        return true;
    }
}
