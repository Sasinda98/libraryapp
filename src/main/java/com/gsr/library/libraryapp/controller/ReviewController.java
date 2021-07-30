package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.Review;
import com.gsr.library.libraryapp.repositories.ReviewRepository;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ReviewController {

    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviews(){
        return (List<Review>) reviewRepository.findAll();
    }
}
