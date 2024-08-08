package org.elibrary.ntuamultimediaproject.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.elibrary.ntuamultimediaproject.models.Book;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class AdminBookDetailsController {

    @FXML
    private Label dateLabel;

    @FXML
    private TextField titleField;

    @FXML
    private TextField writerField;

    @FXML
    private TextField publisherField;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField yearField;

    @FXML
    private ChoiceBox<String> categoryField;

    @FXML
    private TextField copiesField;

    private Book book;

    private Boolean isNewBook;

    @FXML
    public void initialize() {
        categoryField.setItems(FXCollections.observableArrayList(DatabaseManager.getCategories()));

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));
    }

    public void setBook(Book book) {
        this.book = book;
        titleField.setText(book.getTitle());
        writerField.setText(book.getWriter());
        publisherField.setText(book.getPublisher());
        isbnField.setText(book.getIsbn());
        yearField.setText(String.valueOf(book.getYear()));
        copiesField.setText(String.valueOf(book.getCopies()));
        categoryField.getSelectionModel().select(book.getCategoryName());

        isbnField.setEditable(false);
    }

    @FXML
    private void handleUpdateBook(ActionEvent event) {
        isNewBook = false;
        if(validateFields()) {
            book.setTitle(titleField.getText());
            book.setWriter(writerField.getText());
            book.setPublisher(publisherField.getText());
            book.setYear(Integer.parseInt(yearField.getText()));
            book.setCategoryName(categoryField.getValue());
            book.setCopies(Integer.parseInt(copiesField.getText()));

            DatabaseManager.updateBook(book);
            AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "Book Updated", "Your book has been updated.", "/views/AdminBooks.fxml",
                    "Admin - Books", (Node) event.getSource());
        }
    }

    @FXML
    private void handleAddBook(ActionEvent event) {
        isNewBook = true;
        if(validateFields()) {
            book = new Book(
                    titleField.getText(),
                    writerField.getText(),
                    publisherField.getText(),
                    isbnField.getText(),
                    Integer.parseInt(yearField.getText()),
                    categoryField.getValue(),
                    Integer.parseInt(copiesField.getText()),
                    0.0,
                    0
            );
            DatabaseManager.addBook(book);
            AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "Book Added", "Your new book has been added.", "/views/AdminBooks.fxml",
                    "Admin - Book Details", (Node) event.getSource());
        }
    }

    private boolean validateFields() {
        if (titleField.getText().isEmpty() || writerField.getText().isEmpty() || publisherField.getText().isEmpty()
                || isbnField.getText().isEmpty() || yearField.getText().isEmpty() || copiesField.getText().isEmpty()
                || categoryField.getValue() == null) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
            return false;
        }

        if (!Pattern.matches("\\d{4}", yearField.getText())) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "Invalid year format. Please enter a 4-digit year.");
            return false;
        }

        if (!Pattern.matches("\\d+", copiesField.getText())) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "Invalid copies format. Please enter a valid number.");
            return false;
        }

        if(isNewBook && !DatabaseManager.isISBNUnique(isbnField.getText())) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "ISBN number already exists.");
            return false;
        }

        return true;
    }

    @FXML
    private void handleDeleteBook(ActionEvent event) {
        if(AlertManager.showConfirmationAlert("Delete Book",
                "Are you sure you want to delete? Deleting a book will also delete all active borrows related to this book.")) {
            DatabaseManager.deleteBook(book);
            AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "Book Deleted", "Your book has been deleted, along with all active borrows.", "/views/AdminBooks.fxml",
                    "Admin - Book Details", (Node) event.getSource());
        }
    }

    public void handleCancel(ActionEvent event) {
        NavigationManager.navigateTo("/views/AdminBooks.fxml", "Admin - Books", (Node) event.getSource());
    }

    public void backFunction(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/AdminBooks.fxml", "Admin - Books", (Node) mouseEvent.getSource());
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
