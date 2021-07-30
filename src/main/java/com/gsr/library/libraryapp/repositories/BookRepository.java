package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

}
