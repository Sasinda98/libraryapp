package com.gsr.library.libraryapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

public class BookDto {
    @JsonProperty("book_id")
    private Long bookID;
    @JsonProperty("title")
    private String title;
    @JsonProperty("category")
    private String category;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("modified_at")
    private Date modifiedAt;
    @JsonProperty("isbn")
    private Integer isbn;

    public BookDto() {
    }

    public BookDto(Long bookID, String title, String category, Integer quantity, Date createdAt, Date modifiedAt, Integer isbn) {
        this.bookID = bookID;
        this.title = title;
        this.category = category;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isbn = isbn;
    }

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }
}
