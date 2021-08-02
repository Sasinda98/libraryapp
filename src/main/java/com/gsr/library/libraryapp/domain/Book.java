package com.gsr.library.libraryapp.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookID;
    private String title;
    private String category;
    private Integer quantity;
    private Integer isbn;

    @ManyToMany(mappedBy = "borrowedBooks")
    private Set<User> borrowers = new HashSet<>();

    @OneToMany(mappedBy = "book")
    private Set<Review> reviews = new HashSet<>();

    public Book() {
    }

    public Book(String title, String category, Integer quantity, Integer isbn) {
        this.title = title;
        this.category = category;
        this.quantity = quantity;
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

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookID, book.bookID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookID);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookID=" + bookID +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", ISBN=" + isbn +
                '}';
    }
}
