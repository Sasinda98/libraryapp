package com.gsr.library.libraryapp.services;

import com.gsr.library.libraryapp.domain.Review;
import com.gsr.library.libraryapp.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviews(){
        return (List<Review>) reviewRepository.findAll();
    }
}
