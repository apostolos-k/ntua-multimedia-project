package org.elibrary.ntuamultimediaproject.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label totalBorrowsLabel;

    @FXML
    private Label totalBooksLabel;

    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));
        if(welcomeLabel != null) {
            welcomeLabel.setText("Welcome back, " + DatabaseManager.getLoggedInUsername() + "!");
        }
        totalBorrowsLabel.setText(String.valueOf(DatabaseManager.getBorrowedBooksCountForUser(DatabaseManager.getLoggedInUsername())));
        totalBooksLabel.setText(String.valueOf(DatabaseManager.getNumberOfBooks()));
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
