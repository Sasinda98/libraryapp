package com.gsr.library.libraryapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListBookDto {
    @JsonProperty("books")
    private List<BookDto> books;
    @JsonProperty("number_of_books")
    private Integer noOfBooks;

    public ListBookDto() {
    }

    public ListBookDto(Integer noOfBooks, List<BookDto> books) {
        this.books = books;
        this.noOfBooks = noOfBooks;
    }

    public List<BookDto> getBooks() {
        return books;
    }

    public void setBooks(List<BookDto> books) {
        this.books = books;
    }

    public Integer getNoOfBooks() {
        return noOfBooks;
    }

    public void setNoOfBooks(Integer noOfBooks) {
        this.noOfBooks = noOfBooks;
    }
}
