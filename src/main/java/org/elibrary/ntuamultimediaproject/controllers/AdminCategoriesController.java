package org.elibrary.ntuamultimediaproject.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class AdminCategoriesController {

    @FXML
    private Label dateLabel;

    @FXML
    private TextField addCategoryName;

    @FXML
    private TextField editCategoryName;

    @FXML
    ChoiceBox<String> categoryChoiceBox;

    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));

        ObservableList<String> categories = FXCollections.observableArrayList(DatabaseManager.getCategories());
        categoryChoiceBox.setItems(categories);
        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            editCategoryName.setText(newValue);
        });
    }

    @FXML
    private void handleAddCategory(ActionEvent event) {
        if (validateCategoryName(addCategoryName.getText())) {
            DatabaseManager.addNewCategory(addCategoryName.getText());
            AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "Category Added", "New category has been added.", "/views/AdminCategories.fxml",
                    "Admin - Categories", (Node) event.getSource());
        }
    }

    @FXML
    private void handleEditCategory(ActionEvent event) {
        String selectedCategory = categoryChoiceBox.getValue();

        if (selectedCategory == null) {
            AlertManager.showAlert(Alert.AlertType.WARNING, "No Category Selected", "Please select a category to edit.");
            return;
        }
        if (validateCategoryName(editCategoryName.getText())) {
            DatabaseManager.updateCategory(selectedCategory, editCategoryName.getText());
            AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "Category Updated", "Category name has been updated.", "/views/AdminCategories.fxml",
                    "Admin - Categories", (Node) event.getSource());
        }
    }

    @FXML
    private void handleDeleteCategory(ActionEvent event) {
        if(AlertManager.showConfirmationAlert("Delete Category",
                "Are you sure you want to delete? Deleting a category will also delete all books related to this category " +
                        "and all active borrows on these books.")) {
            if(categoryChoiceBox.getValue() == null) {
                System.out.println("Please select a category to delete.");
            } else {
                DatabaseManager.deleteCategory(categoryChoiceBox.getValue());
            }
            AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "Category Deleted", "Category has been deleted.",
                    "/views/AdminCategories.fxml", "Admin - Categories", (Node) event.getSource());
        }
    }

    private boolean validateCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid category name.");
            return false;
        }

        if (!Pattern.matches("[a-zA-Z0-9\\s]+", categoryName)) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "Category name can only contain letters, numbers, and spaces.");
            return false;
        }

        if(!editCategoryName.getText().equals(categoryChoiceBox.getValue())
                && !DatabaseManager.isCategoryNameUnique(categoryName)) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "Category name already exists.");
            return false;
        }
        return true;
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
