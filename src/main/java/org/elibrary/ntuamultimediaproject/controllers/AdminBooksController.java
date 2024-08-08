package org.elibrary.ntuamultimediaproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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

public class AdminBooksController {

    @FXML
    private Label dateLabel;

    @FXML
    private VBox booksVBox;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));

        ObservableList<String> categories = FXCollections.observableArrayList(DatabaseManager.getCategories());
        categoryChoiceBox.setItems(categories);

        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            booksVBox.getChildren().clear();
            filterBooksByCategory(newValue);
        });
    }

    public void filterBooksByCategory(String category) {
        List<Book> books = DatabaseManager.getBooksByCategory(category);

        if (books.isEmpty()) {
            Label noResultsLabel = new Label("No books found, please add.");
            noResultsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
            booksVBox.setAlignment(Pos.CENTER);
            booksVBox.getChildren().add(noResultsLabel);
        } else {
            for (Book book : books) {
                BorderPane borderPane = new BorderPane();
                borderPane.setStyle("-fx-border-color: #0e6aff; -fx-border-width: 2px; -fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px; -fx-background-color: white; -fx-padding: 5px;");

                Label titleLabel = new Label(book.getTitle());
                titleLabel.setStyle("-fx-font-weight: bold;");
                Label writerLabel = new Label("by " + book.getWriter());
                Label publisherLabel = new Label("Publisher: " + book.getPublisher());
                Label isbnLabel = new Label("ISBN: " + book.getIsbn());
                Label yearOfPublish = new Label("Year of Publish: " + book.getYear());
                Label categoryLabel = new Label("Category: " + book.getCategoryName());
                Label copiesLabel = new Label("Copies: " + book.getCopies());

                VBox.setMargin(borderPane, new Insets(5));

                VBox vbox = new VBox(titleLabel, writerLabel, publisherLabel, isbnLabel, yearOfPublish, categoryLabel, copiesLabel);
                vbox.setAlignment(Pos.CENTER);
                borderPane.setCenter(vbox);

                borderPane.setOnMouseClicked(event -> handleBookSelection(book));

                booksVBox.getChildren().add(borderPane);
            }
        }
    }

    private void handleBookSelection(Book selectedBook) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminBookEdit.fxml"));
            Parent bookDetailsRoot = loader.load();

            AdminBookDetailsController adminBookDetailsController = loader.getController();
            adminBookDetailsController.setBook(selectedBook);

            Scene bookDetailsScene = new Scene(bookDetailsRoot);

            Stage stage = (Stage) booksVBox.getScene().getWindow();
            stage.setScene(bookDetailsScene);
            stage.setTitle("Admin- Edit Book Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
    private void handleAddNewBook(ActionEvent event) {
        NavigationManager.navigateTo("/views/AdminBookAdd.fxml", "Admin - Add a Book", (Node) event.getSource());
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
