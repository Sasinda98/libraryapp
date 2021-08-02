package com.gsr.library.libraryapp.controller;

import com.gsr.library.libraryapp.domain.Review;
import com.gsr.library.libraryapp.repositories.ReviewRepository;
import com.gsr.library.libraryapp.services.ReviewService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public List<Review> getReviews(){
        return reviewService.getReviews();
    }
}
