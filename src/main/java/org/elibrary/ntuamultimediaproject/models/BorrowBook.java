package org.elibrary.ntuamultimediaproject.models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class BorrowBook implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String bookISBN;
    private String title;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowBook(String username, String bookISBN, String title, LocalDate borrowDate, LocalDate returnDate) {
        this.username = username;
        this.bookISBN = bookISBN;
        this.title = title;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "BorrowBook{" +
                "username='" + username + '\'' +
                ", bookISBN='" + bookISBN + '\'' +
                ", title='" + title + '\'' +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
