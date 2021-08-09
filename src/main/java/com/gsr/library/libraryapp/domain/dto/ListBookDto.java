package com.gsr.library.libraryapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListBookDto that = (ListBookDto) o;
        return Objects.equals(books, that.books) && Objects.equals(noOfBooks, that.noOfBooks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(books, noOfBooks);
    }

    @Override
    public String toString() {
        return "ListBookDto{" +
                "books=" + books +
                ", noOfBooks=" + noOfBooks +
                '}';
    }
}
