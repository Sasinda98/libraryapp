package com.gsr.library.libraryapp.domain;

import org.graalvm.compiler.lir.LIRInstruction;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reviewID;
    private String title;
    private String review;
    private String rating;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User user;

    public Review() {
    }

    public Review(Long reviewID, String title, String review, String rating) {
        this.reviewID = reviewID;
        this.title = title;
        this.review = review;
        this.rating = rating;
    }

    public Long getReviewID() {
        return reviewID;
    }

    public void setReviewID(Long reviewID) {
        this.reviewID = reviewID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(reviewID, review.reviewID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewID);
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewID=" + reviewID +
                ", title='" + title + '\'' +
                ", review='" + review + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
