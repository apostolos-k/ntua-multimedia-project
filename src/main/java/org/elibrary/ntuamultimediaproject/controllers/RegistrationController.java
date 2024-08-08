package org.elibrary.ntuamultimediaproject.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.elibrary.ntuamultimediaproject.models.User;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.util.regex.Pattern;

public class RegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField adtField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    void handleRegistration(ActionEvent event) {
        String validationMessage = isValid();
        if (!validationMessage.isEmpty()) {
            AlertManager.showAlert(Alert.AlertType.ERROR, "Error", validationMessage);
            return;
        }

        if(AlertManager.showConfirmationAlert("Confirm",
                "Please confirm your username is correct, it cannot be changed in the future.")) {
            User newUser = new User(usernameField.getText(), passwordField.getText(), emailField.getText(), firstNameField.getText(), lastNameField.getText(), adtField.getText());

            DatabaseManager.addUser(newUser);

            AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "Registration Successful", "You are registered successfully, now you can login.",
                    "/views/Login.fxml", "eLibrary - Sign In", usernameField);

            System.out.println("INFO: User registered successfully.");
        }
    }

    private String isValid() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String adt = adtField.getText();
        String email = emailField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if any field is empty
        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || adt.isEmpty() || email.isEmpty() || confirmPassword.isEmpty()) {
            return "All fields are required.";
        }

        // Validate username
        if (!Pattern.matches("^[a-zA-Z]\\w{4,}$", username)) {
            return "Username must start with a letter and be at least 5 characters long.";
        }

        // Username uniqueness
        if (!DatabaseManager.isUsernameUnique(username)) {
            return "Username is already in use.";
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
        if (!DatabaseManager.isEmailUnique(email)) {
            return "Email is already in use.";
        }

        // Validate ADT format
        if (!Pattern.matches("^[A-Z]{3}\\d{6}$", adt)) {
            return "Greek ADT must have the following format AAA111111 (three letters and six numbers).";
        }

        // ADT uniqueness
        if (!DatabaseManager.isAdtUnique(adt)) {
            return "ADT is already in use.";
        }

        // Validate password
        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,}$", password)) {
            return "Password must have at least 5 characters with one number and one letter.";
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match. Check again.";
        }

        return "";
    }

    public void backFunction(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/Login.fxml", "eLibrary - Sign In", (Node) mouseEvent.getSource());
    }
}
