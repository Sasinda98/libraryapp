package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.exceptions.OperationStoppedException;
import com.gsr.library.libraryapp.exceptions.ValidationException;
import com.gsr.library.libraryapp.micellaneous.Validator;
import com.gsr.library.libraryapp.repositories.BookRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final UserService userService;

    public BookServiceImpl(BookRepository bookRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
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
    public void updateBook(Book book) throws ValidationException, OperationStoppedException {
        Optional<Book> bookOptional = bookRepository.findById(book.getBookID());

        if (!bookOptional.isPresent()){
            throw new OperationStoppedException("No such book found to update.");
        }

        //Implement validation here.
        if(!Validator.getInstance().isBookValid(book)){
            throw new ValidationException("Invalid book details.");
        }

        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteBook(Long bookID) throws OperationStoppedException {
        Optional<Book> bookOptional = bookRepository.findById(bookID);

        if (!bookOptional.isPresent()){
            throw new OperationStoppedException("No such book found to delete.");
        }

        bookRepository.delete(bookOptional.get());
    }

    @Override
    @Transactional
    public void borrowBook(Long userID, Long bookID) throws OperationStoppedException {
        Optional<User> optionalUser = userService.getUserByID(userID);
        Optional<Book> optionalBook = bookRepository.findById(bookID);
        Boolean isBorrowed = bookRepository.isBookBorrowedByUser(userID, bookID);

        if(!optionalUser.isPresent() || !optionalBook.isPresent())
            throw new OperationStoppedException("User or book specified does not exist.");
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
    }

    @Override
    @Transactional
    public void returnBook(Long userID, Long bookID) throws OperationStoppedException {
        Optional<User> optionalUser = userService.getUserByID(userID);
        Optional<Book> optionalBook = bookRepository.findById(bookID);
        Boolean isBorrowed = bookRepository.isBookBorrowedByUser(userID, bookID);

        if(!isBorrowed)
            throw new OperationStoppedException("You can only return borrowed books.");
        if(!optionalUser.isPresent() || !optionalBook.isPresent())
            throw new OperationStoppedException("User or book being returned does not exist.");

        //Green light to return.
        User user = optionalUser.get();
        Book book = optionalBook.get();

        book.setQuantity(book.getQuantity() + 1);
        user.getBorrowedBooks().remove(book);
        book.getBorrowers().remove(user);

        bookRepository.save(book);
        userService.updateUser(user);
    }
}
