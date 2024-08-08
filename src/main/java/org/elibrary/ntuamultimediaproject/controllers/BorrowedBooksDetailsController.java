package org.elibrary.ntuamultimediaproject.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import org.elibrary.ntuamultimediaproject.models.BorrowBook;
import org.elibrary.ntuamultimediaproject.models.Rating;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowedBooksDetailsController {

    @FXML
    private Label dateLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label borrowDateLabel;

    @FXML
    private Label returnDateLabel;

    @FXML
    private ChoiceBox<Integer> ratingChoiceBox;

    @FXML
    private TextArea commentTextArea;

    private BorrowBook borrowBook;

    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));
    }

    public void backFunction(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/BorrowedBooks.fxml", "Borrowed Books", (Node) mouseEvent.getSource());
    }

    public void setBorrowedBook(BorrowBook borrowBook) {
        this.borrowBook = borrowBook;
        titleLabel.setText(borrowBook.getTitle());
        isbnLabel.setText("ISBN: " + borrowBook.getBookISBN());
        borrowDateLabel.setText("Borrow date: " + borrowBook.getBorrowDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        returnDateLabel.setText("Return date: " + borrowBook.getReturnDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

        ratingChoiceBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        Rating existingRating = DatabaseManager.findUserBookRating(DatabaseManager.getLoggedInUsername(), borrowBook.getBookISBN());
        if (existingRating != null) {
            ratingChoiceBox.getSelectionModel().select(existingRating.getRating() - 1);
            commentTextArea.setText(existingRating.getComment());
        } else {
            ratingChoiceBox.getSelectionModel().select(4);
            commentTextArea.setPromptText("Add your comment here");
        }
    }

    @FXML
    private void handleSubmitRating() {
        int rating = ratingChoiceBox.getValue();
        String comment = commentTextArea.getText();

        String username = DatabaseManager.getLoggedInUsername();
        String bookISBN = borrowBook.getBookISBN();

        DatabaseManager.rateBook(username, bookISBN, rating, comment);
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
