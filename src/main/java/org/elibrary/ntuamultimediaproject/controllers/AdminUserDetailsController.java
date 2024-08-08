package org.elibrary.ntuamultimediaproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.elibrary.ntuamultimediaproject.models.User;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class AdminUserDetailsController {

    @FXML
    private Label dateLabel;

    @FXML
    public TextField usernameField;

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;

    @FXML
    public TextField adtField;

    @FXML
    public TextField emailField;

    @FXML
    public PasswordField oldPasswordField;

    @FXML
    public PasswordField newPasswordField;

    @FXML
    public PasswordField confirmNewPasswordField;

    private User user;

    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));
    }

    public void setUser(User user) {
        this.user = user;
        usernameField.setText(user.getUsername());
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        adtField.setText(user.getAdt());
        emailField.setText(user.getEmail());
    }

    @FXML
    private void handleUpdateUser(ActionEvent event) {
        String validationMessage = validateFields();
        if (!validationMessage.isEmpty()) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", validationMessage);
            return;
        }

        user.setFirstName(firstNameField.getText());
        user.setLastName(lastNameField.getText());
        user.setAdt(adtField.getText());
        user.setEmail(emailField.getText());

        DatabaseManager.updateUser(user);
        AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "User Updated", "User has been updated.", "/views/AdminUsers.fxml",
                "Admin - Users", (Node) event.getSource());
    }

    private String validateFields() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String adt = adtField.getText();
        String email = emailField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || adt.isEmpty() || email.isEmpty()) {
            return "All fields are required.";
        }

        // Validate full name
        if (!Pattern.matches("^[a-zA-Z]+$", firstName)) {
            return "First name must consist of only one word with only letters.";
        }

        // Validate last name
        if (!Pattern.matches("^[a-zA-Z]+$", lastName)) {
            return "Last name must consist of only one word with only letters.";
        }

        // Validate email format
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email)) {
            return "Invalid email format.";
        }

        // Email uniqueness
        if (!user.getEmail().equals(email)
                && !DatabaseManager.isEmailUnique(email)) {
            return "Email is already in use.";
        }

        // Validate ADT format
        if (!Pattern.matches("^[A-Z]{3}\\d{6}$", adt)) {
            return "Greek ADT must have the following format AAA111111 (three letters and six numbers).";
        }

        // ADT uniqueness
        if (!user.getAdt().equals(adt)
                && !DatabaseManager.isAdtUnique(adt)) {
            return "ADT is already in use.";
        }

        return "";
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        if(AlertManager.showConfirmationAlert("Delete User",
                "Are you sure you want to delete? Deleting a user will also delete all active borrows related to this user.")) {
            DatabaseManager.deleteUser(user);
            AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "User Deleted", "User has been deleted, along with all active borrows.", "/views/AdminUsers.fxml",
                    "Admin - Users", (Node) event.getSource());
        }
    }

    @FXML
    public void handlePasswordChange(ActionEvent event) {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (validatePasswordChange(oldPassword, newPassword, confirmNewPassword)) {
            user.setPassword(newPassword);
            DatabaseManager.updateUser(user);

            AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "Password Changed", "Your password has been changed successfully.", "/views/AdminUsers.fxml",
                    "Admin - Users", (Node) event.getSource());
        }
    }

    private boolean validatePasswordChange(String oldPassword, String newPassword, String confirmNewPassword) {
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "Please fill all password fields.");
            return false;
        }

        if (!oldPassword.equals(user.getPassword())) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "Old password is incorrect.");
            return false;
        }

        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,}$", newPassword)) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "Password must have at least 5 characters with one number and one letter.");
            return false;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            return false;
        }

        return true;
    }

    @FXML
    public void backFunction(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/AdminUsers.fxml", "Admin - Users", (Node) mouseEvent.getSource());
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
