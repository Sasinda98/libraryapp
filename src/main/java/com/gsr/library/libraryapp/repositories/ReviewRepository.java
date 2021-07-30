package com.gsr.library.libraryapp.repositories;

import com.gsr.library.libraryapp.domain.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

}
