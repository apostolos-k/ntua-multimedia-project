package org.elibrary.ntuamultimediaproject.models;

import java.io.Serial;
import java.io.Serializable;

public class Rating implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String bookISBN;
    private int rating;
    private String comment;

    public Rating(String username, String bookISBN, int rating, String comment) {
        this.username = username;
        this.bookISBN = bookISBN;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "username='" + username + '\'' +
                ", bookISBN='" + bookISBN + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
