package com.gsr.library.libraryapp.cmdrunner;

import com.gsr.library.libraryapp.controller.BookController;
import com.gsr.library.libraryapp.controller.ReviewController;
import com.gsr.library.libraryapp.controller.UserController;
import com.gsr.library.libraryapp.domain.Book;
import com.gsr.library.libraryapp.domain.User;
import com.gsr.library.libraryapp.repositories.BookRepository;
import com.gsr.library.libraryapp.repositories.ReviewRepository;
import com.gsr.library.libraryapp.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Component
public class CMDRunner implements CommandLineRunner {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public CMDRunner(BookRepository bookRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Book b1 = new Book("Garry", "Fiction", 3, 1234, new Date(), new Date());
        Book b2 = new Book("Bravo Two Zero", "Non Fiction", 3, 2345, new Date(), new Date());
        Book b3 = new Book("Ghost Patrol", "Non Fiction", 2, 3456, new Date(), new Date());
        Book b4 = new Book("Frankenstein", "Fiction", 1, 4567, new Date(), new Date());
        Book b5 = new Book("Glass Hotel", "Science Fiction", 5, 5678, new Date(), new Date());
        Book b6 = new Book("Sherlock Holmes", "Mystery", 8, 6789, new Date(), new Date());


        User u1 = new User("Gayal", "Rupasinghe", "gayal@domain.com");
        User u2 = new User("John", "Doe", "john@domain.com");
        User u3 = new User("Lewis", "Hamilton", "lewis@domain.com");

        b1.getBorrowers().add(u1);
        u1.getBorrowedBooks().add(b1);
        bookRepository.save(b1);
        userRepository.save(u1);

        b2.getBorrowers().add(u1);
        u1.getBorrowedBooks().add(b2);
        bookRepository.save(b2);
        userRepository.save(u1);
    }
}
