package org.elibrary.ntuamultimediaproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.elibrary.ntuamultimediaproject.models.Book;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SearchController {

    @FXML
    private Label dateLabel;

    @FXML
    private TextField titleField;

    @FXML
    private TextField writerField;

    @FXML
    private TextField yearOfPublishField;

    @FXML
    private VBox searchResultsVBox;

    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));
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
    private void handleSearchButtonClick() {
        String title = titleField.getText();
        String writer = writerField.getText();
        String yearOfPublish = yearOfPublishField.getText();

        List<Book> searchResults = DatabaseManager.searchBooks(title, writer, yearOfPublish);

        searchResultsVBox.getChildren().clear();

        if (searchResults.isEmpty()) {
            Label noResultsLabel = new Label("No results found");
            noResultsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
            searchResultsVBox.setAlignment(Pos.CENTER);
            searchResultsVBox.getChildren().add(noResultsLabel);
        } else {
            for (Book book : searchResults) {
                BorderPane borderPane = new BorderPane();
                borderPane.setStyle("-fx-border-color: #8364d3; -fx-border-width: 2px; -fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px; -fx-background-color: white; -fx-padding: 5px;");

                Label titleLabel = new Label(book.getTitle());
                titleLabel.setStyle("-fx-font-weight: bold;");
                Label writerLabel = new Label("by " + book.getWriter());
                Label isbnLabel = new Label("ISBN: " + book.getIsbn());

                HBox ratingBox = new HBox();
                Label ratingLabel = new Label("Rating: " + String.format("%.2f", book.getRating()));
                Label numRatingsLabel = new Label("from " + book.getNumRatings() + " users");
                ratingBox.getChildren().addAll(ratingLabel, new Label(" "), numRatingsLabel);
                ratingBox.setAlignment(Pos.CENTER);

                VBox.setMargin(borderPane, new Insets(5));

                VBox vbox = new VBox(titleLabel, writerLabel, isbnLabel, ratingBox);
                vbox.setAlignment(Pos.CENTER);
                borderPane.setCenter(vbox);

                borderPane.setOnMouseClicked(event -> handleBookSelection(book));

                searchResultsVBox.getChildren().add(borderPane);
            }
        }
    }

    private void handleBookSelection(Book selectedBook) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BookDetails.fxml"));
            Parent bookDetailsRoot = loader.load();

            BookDetailsController bookDetailsController = loader.getController();
            bookDetailsController.setBook(selectedBook);

            Scene bookDetailsScene = new Scene(bookDetailsRoot);

            Stage stage = (Stage) searchResultsVBox.getScene().getWindow();
            stage.setScene(bookDetailsScene);
            stage.setTitle("Book Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
