package org.elibrary.ntuamultimediaproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.elibrary.ntuamultimediaproject.models.User;
import org.elibrary.ntuamultimediaproject.utils.AlertManager;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;
import org.elibrary.ntuamultimediaproject.utils.NavigationManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminUsersController {

    @FXML
    private Label dateLabel;

    @FXML
    private VBox usersVBox;

    @FXML
    public void initialize() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateLabel.setText(currentDate.format(formatter));

        List<User> users = DatabaseManager.getUsers();

        usersVBox.getChildren().clear();

        if (users.isEmpty()) {
            Label noUsersLabel = new Label("No users found");
            noUsersLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
            usersVBox.setAlignment(Pos.CENTER);
            usersVBox.getChildren().add(noUsersLabel);
        } else {
            for (User user : users) {
                BorderPane borderPane = new BorderPane();

                VBox.setMargin(borderPane, new Insets(5));

                if(user.getAdmin()) {
                    borderPane.setStyle("-fx-border-color: #f60606; -fx-border-width: 2px; -fx-border-radius: 20px; " +
                            "-fx-background-radius: 20px; -fx-background-color: white; -fx-padding: 5px;");

                    Label adminLabel = new Label("Admin User");
                    adminLabel.setStyle("-fx-font-weight: bold;");
                    Label usernameLabel = new Label(user.getUsername());

                    VBox vbox = new VBox(adminLabel, usernameLabel);
                    vbox.setAlignment(Pos.CENTER);
                    borderPane.setCenter(vbox);
                } else {
                    borderPane.setStyle("-fx-border-color: #0e6aff; -fx-border-width: 2px; -fx-border-radius: 20px; " +
                        "-fx-background-radius: 20px; -fx-background-color: white; -fx-padding: 5px;");

                    Label usernameLabel = new Label(user.getUsername());
                    usernameLabel.setStyle("-fx-font-weight: bold;");
                    Label nameLabel = new Label(user.getFirstName() + " " + user.getLastName());
                    Label adtLabel = new Label("ADT: " + user.getAdt());
                    Label emailLabel = new Label("email : " + user.getEmail());

                    VBox vbox = new VBox(usernameLabel, nameLabel, adtLabel, emailLabel);
                    vbox.setAlignment(Pos.CENTER);
                    borderPane.setCenter(vbox);
                }

                borderPane.setOnMouseClicked(event -> handleUserSelection(user));

                usersVBox.getChildren().add(borderPane);
            }
        }
    }

    private void handleUserSelection(User selectedUser) {
        try {
            if(selectedUser.getAdmin()) {
                AlertManager.showAlert(Alert.AlertType.ERROR, "Admin User", "You cannot edit admin user details.");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminUserDetails.fxml"));
                Parent userDetailsRoot = loader.load();

                AdminUserDetailsController adminUserDetailsController = loader.getController();
                adminUserDetailsController.setUser(selectedUser);

                Scene userDetailsScene = new Scene(userDetailsRoot);

                Stage stage = (Stage) usersVBox.getScene().getWindow();
                stage.setScene(userDetailsScene);
                stage.setTitle("Admin - User Details");
                stage.show();
            }
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
    public void handleLogout(MouseEvent mouseEvent) {
        if(AlertManager.showConfirmationAlert("Logout",
                "Are you sure you want to logout?")) {
            DatabaseManager.setLoggedInUsername(null);
            NavigationManager.navigateTo("/views/Login.fxml", "eLibrary - Sign in", (Node) mouseEvent.getSource());
        }
    }
}
