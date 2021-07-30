package com.gsr.library.libraryapp.cmdrunner;

import com.gsr.library.libraryapp.controller.BookController;
import com.gsr.library.libraryapp.controller.UserController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class CMDRunner implements CommandLineRunner {
    private final BookController bookController;
    private final UserController userController;

    public CMDRunner(BookController bookController, UserController userController) {
        this.bookController = bookController;
        this.userController = userController;
    }

    @Override
    public void run(String... args) throws Exception {
        bookController.getBooks();
        userController.getUsers();
    }
}
