package org.elibrary.ntuamultimediaproject.utils;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

import static org.elibrary.ntuamultimediaproject.utils.NavigationManager.navigateTo;

/**
 * Utility class for managing alert dialogs in the application.
 */
public class AlertManager {

    /**
     * Show a simple alert dialog with the specified alert type, title, and message.
     *
     * @param alertType The type of the alert dialog (e.g., INFORMATION, WARNING, ERROR).
     * @param title     The title of the alert dialog.
     * @param message   The message to display in the alert dialog.
     */
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show an alert dialog with the specified alert type, title, message, and navigate to a new screen upon closing.
     *
     * @param alertType       The type of the alert dialog (e.g., INFORMATION, WARNING, ERROR).
     * @param title           The title of the alert dialog.
     * @param message         The message to display in the alert dialog.
     * @param fxmlPath        The path to the FXML file of the screen to navigate to.
     * @param navigationTitle The title of the screen to navigate to.
     * @param node            The JavaFX node used to get the stage for navigation.
     */
    public static void showAlertAndNavigate(Alert.AlertType alertType, String title, String message,
                                            String fxmlPath, String navigationTitle, Node node) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.setOnCloseRequest(event -> {
            navigateTo(fxmlPath, navigationTitle, node);
        });

        alert.showAndWait();
    }

    /**
     * Show a confirmation alert dialog with the specified title and message.
     *
     * @param title   The title of the confirmation dialog.
     * @param message The message to display in the confirmation dialog.
     * @return {@code true} if the user clicks OK, {@code false} otherwise.
     */
    public static boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
