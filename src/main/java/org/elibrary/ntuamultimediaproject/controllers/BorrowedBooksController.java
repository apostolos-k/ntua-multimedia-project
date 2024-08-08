package org.elibrary.ntuamultimediaproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.elibrary.ntuamultimediaproject.models.BorrowBook;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BorrowedBooksController {

    @FXML
    private Label dateLabel;

    @FXML
    private VBox borrowedBooksVBox;

    @FXML
    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));

        List<BorrowBook> borrowedBooks = DatabaseManager.findAllActiveBorrowedBooksByUsername(DatabaseManager.getLoggedInUsername());

        if (borrowedBooks.isEmpty()) {
            Label noBorrowsLabel = new Label("No active borrows");
            noBorrowsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            borrowedBooksVBox.getChildren().add(noBorrowsLabel);
        } else {
            for (BorrowBook borrowBook : borrowedBooks) {
                BorderPane borderPane = new BorderPane();
                borderPane.setStyle("-fx-border-color: #8364d3; -fx-border-width: 2px; -fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px; -fx-background-color: white; -fx-padding: 5px;");

                Label titleLabel = new Label(borrowBook.getTitle());
                titleLabel.setStyle("-fx-font-weight: bold;");
                Label isbnLabel = new Label("ISBN: " + borrowBook.getBookISBN());
                Label borrowDateLabel = new Label("Borrow Date: " + borrowBook.getBorrowDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                Label returnDateLabel = new Label("Return Date: " + borrowBook.getReturnDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

                VBox.setMargin(borderPane, new Insets(5));

                VBox vbox = new VBox(titleLabel, isbnLabel, borrowDateLabel, returnDateLabel);
                vbox.setAlignment(Pos.CENTER);
                borderPane.setCenter(vbox);

                borderPane.setOnMouseClicked(event -> {
                    try {
                        handleBorrowedBookSelection(borrowBook);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                borrowedBooksVBox.getChildren().add(borderPane);
            }
        }
    }


    private void handleBorrowedBookSelection(BorrowBook selectedBook) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BorrowedBookDetails.fxml"));
        Parent borrowedBookDetailsRoot = loader.load();

        BorrowedBooksDetailsController borrowedBooksDetailsController = loader.getController();
        borrowedBooksDetailsController.setBorrowedBook(selectedBook);

        Scene borrowedBookDetailsScene = new Scene(borrowedBookDetailsRoot);

        Stage stage = (Stage) borrowedBooksVBox.getScene().getWindow();
        stage.setScene(borrowedBookDetailsScene);
        stage.setTitle("Rate a Book");
        stage.show();
    }

    @FXML
    private void handleHutIconClick(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/Dashboard.fxml", "Dashboard", (Node) mouseEvent.getSource());
    }

    @FXML
    private void handleBookIconClick(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/BorrowedBooks.fxml", "Borrowed Books", (Node) mouseEvent.getSource());
    }

    @FXML
    private void handleSearchIconClick(MouseEvent mouseEvent) {
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
