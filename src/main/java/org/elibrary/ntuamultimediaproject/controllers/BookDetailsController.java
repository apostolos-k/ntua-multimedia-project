package org.elibrary.ntuamultimediaproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.elibrary.ntuamultimediaproject.models.Book;
import org.elibrary.ntuamultimediaproject.models.Rating;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookDetailsController {

    @FXML
    private Label dateLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label writerLabel;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label copiesLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label numberOfRatingsLabel;

    @FXML
    VBox commentsVBox;

    private Book book;

    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));
    }

    public void setBook(Book book) {
        this.book = book;
        titleLabel.setText(book.getTitle());
        writerLabel.setText("Writer: " + book.getWriter());
        publisherLabel.setText("Publisher: " + book.getPublisher());
        isbnLabel.setText("ISBN: " + book.getIsbn());
        yearLabel.setText("Year: " + book.getYear());
        categoryLabel.setText("Category: " + book.getCategoryName());
        copiesLabel.setText("The are " + book.getCopies() + " copies available");
        ratingLabel.setText("Rating: " + String.format("%.2f", book.getRating()) + " out of 5");
        numberOfRatingsLabel.setText("by " + book.getNumRatings() + " users");
        List<Rating> ratings = DatabaseManager.getBookRatings(book);
        displayComments(ratings);
    }

    private void displayComments(List<Rating> ratings) {
        commentsVBox.getChildren().clear();
        if (ratings != null && !ratings.isEmpty()) {
            for (Rating rating : ratings) {
                String username = rating.getUsername();
                String truncatedUsername = truncateUsername(username);
                String comment = rating.getComment();
                if (!comment.isEmpty()) {
                    Label commentLabel = new Label(truncatedUsername + " wrote: " + comment);
                    commentsVBox.getChildren().add(commentLabel);
                }
            }
        } else {
            Label noCommentsLabel = new Label("No comments available");
            commentsVBox.getChildren().add(noCommentsLabel);
        }
    }

    private String truncateUsername(String username) {
        if (username.length() > 3) {
            return username.substring(0, 3) + "***";
        } else {
            return username;
        }
    }

    @FXML
    private void handleBorrowBook(ActionEvent event) {
        String username = DatabaseManager.getLoggedInUsername();
        String bookISBN = book.getIsbn();
        DatabaseManager.borrowBook(username, bookISBN);
        copiesLabel.setText("The are " + book.getCopies() + " copies available");
    }

    public void backFunction(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/Search.fxml", "Search", (Node) mouseEvent.getSource());
    }

    @FXML
    public void handleLogout(MouseEvent mouseEvent) {
        if(AlertManager.showConfirmationAlert("Logout",
                "Are you sure you want to logout?")) {
            DatabaseManager.setLoggedInUsername(null);
            NavigationManager.navigateTo("/views/Login.fxml", "eLibrary - Sign in", (Node) mouseEvent.getSource());
        }
    }
}
