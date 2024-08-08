package org.elibrary.ntuamultimediaproject.models;

import java.io.Serial;
import java.io.Serializable;

public class Book implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private String writer;
    private String publisher;
    private String isbn;    // That will be the ID of the Book
    private int year;
    private String categoryName;
    private int copies;
    private double rating;
    private int numRatings;

    public Book(String title, String writer, String publisher, String isbn, int year, String categoryName, int copies, double rating, int numRatings) {
        this.title = title;
        this.writer = writer;
        this.publisher = publisher;
        this.isbn = isbn;
        this.year = year;
        this.categoryName = categoryName;
        this.copies = copies;
        this.rating = rating;
        this.numRatings = numRatings;
    }

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", writer='" + writer + '\'' +
                ", publisher='" + publisher + '\'' +
                ", isbn='" + isbn + '\'' +
                ", year=" + year +
                ", categoryName='" + categoryName + '\'' +
                ", copies=" + copies +
                ", rating=" + rating +
                ", numRatings=" + numRatings +
                '}';
    }
}
