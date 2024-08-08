package org.elibrary.ntuamultimediaproject.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.elibrary.ntuamultimediaproject.models.Book;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label usernameErrorLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Label loginErrorLabel;

    @FXML
    private VBox topRatedVBox;

    @FXML
    void handleLogin() {
        usernameErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        loginErrorLabel.setVisible(false);

        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean userExists = DatabaseManager.checkUserExists(username);

        if (!userExists) {
            usernameErrorLabel.setText("Username does not exist");
            usernameErrorLabel.setVisible(true);
        } else {
            boolean passwordMatches = DatabaseManager.checkPasswordMatches(username, password);

            if (!passwordMatches) {
                passwordErrorLabel.setText("Wrong password");
                passwordErrorLabel.setVisible(true);
            } else {
                System.out.println("INFO: Login successful.");
                DatabaseManager.setLoggedInUsername(username);

                if(DatabaseManager.isUserAdmin(username)) {
                    AlertManager.showAlertAndNavigate(Alert.AlertType.INFORMATION, "Admin", "Admin account detected. You will be redirected to Admin Backoffice.",
                            "/views/AdminBooks.fxml", "Admin - Book Management", usernameField);
                } else {
                    NavigationManager.navigateTo("/views/Dashboard.fxml", "Dashboard", usernameField);
                }
            }
        }
    }

    public void handleRegister(MouseEvent mouseEvent) {
        NavigationManager.navigateTo("/views/Registration.fxml", "eLibrary - Sign Up", (Node) mouseEvent.getSource());
    }

    @FXML
    public void initialize() {
        List<Book> topRatedBooks = DatabaseManager.getTopRatedBooks(5);

        for (Book book : topRatedBooks) {
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

            topRatedVBox.getChildren().add(borderPane);
        }
    }

}
