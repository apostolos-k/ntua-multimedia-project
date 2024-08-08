package org.elibrary.ntuamultimediaproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.elibrary.ntuamultimediaproject.models.BorrowBook;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminBorrowsController {

    @FXML
    private Label dateLabel;

    @FXML
    VBox borrowsVBox;

    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));

        List<BorrowBook> borrowBooks = DatabaseManager.getBorrowedBooks();

        if (borrowBooks.isEmpty()) {
            Label noResultsLabel = new Label("No active borrows found.");
            noResultsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
            borrowsVBox.setAlignment(Pos.CENTER);
            borrowsVBox.getChildren().add(noResultsLabel);
        } else {
            for (BorrowBook borrowBook : borrowBooks) {
                BorderPane borderPane = new BorderPane();
                borderPane.setStyle("-fx-border-color: #0e6aff; -fx-border-width: 2px; -fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px; -fx-background-color: white; -fx-padding: 5px;");

                Label titleLabel = new Label(borrowBook.getTitle());
                titleLabel.setStyle("-fx-font-weight: bold;");
                Label usernameLabel = new Label("by " + borrowBook.getUsername());
                Label isbnLabel = new Label("ISBN: " + borrowBook.getBookISBN());
                Label borrowDateLabel = new Label("Borrow date: " + borrowBook.getBorrowDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

                Label returnDateLabel = new Label("Return date: " + borrowBook.getReturnDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

                VBox.setMargin(borderPane, new Insets(5));

                VBox vbox = new VBox(titleLabel, usernameLabel, isbnLabel, borrowDateLabel, returnDateLabel);
                vbox.setAlignment(Pos.CENTER);

                Button deleteButton = new Button("End Borrow");
                deleteButton.setOnAction(event -> handleDeleteButtonClick(event, borrowBook)); // Pass the event and borrowBook
                deleteButton.setStyle("-fx-background-color: red; -fx-background-radius: 5; -fx-text-fill: #ffffff;");
                vbox.getChildren().add(deleteButton);

                borderPane.setCenter(vbox);

                borrowsVBox.getChildren().add(borderPane);
            }
        }
    }

    private void handleDeleteButtonClick(ActionEvent event, BorrowBook borrowBook) {
        if(AlertManager.showConfirmationAlert("Terminate Borrow",
                "Are you sure you want to terminate this active borrow? This action cannot be reverted.")) {
            DatabaseManager.deleteBorrow(borrowBook.getUsername(), borrowBook.getBookISBN());
            AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "Borrow Terminated", "Active borrow has now been terminated.", "/views/AdminBorrows.fxml",
                    "Admin - Borrows", (Node) event.getSource());
        }
    }

    @FXML
    private void handleBookIconClick(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/AdminBooks.fxml", "Admin - Books", (Node) mouseEvent.getSource());
    }

    @FXML
    private void handleCategoryIconClick(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/AdminCategories.fxml", "Admin - Categories", (Node) mouseEvent.getSource());
    }

    @FXML
    private void handleUserIconClick(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/AdminUsers.fxml", "Admin - Users", (Node) mouseEvent.getSource());
    }

    @FXML
    private void handleBorrowIconClick(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/AdminBorrows.fxml", "Admin - Borrows", (Node) mouseEvent.getSource());
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
