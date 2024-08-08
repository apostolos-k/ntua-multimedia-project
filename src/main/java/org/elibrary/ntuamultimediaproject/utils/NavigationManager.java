package org.elibrary.ntuamultimediaproject.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for managing screen navigation in the application.
 */
public class NavigationManager {

    /**
     * Navigate to a new screen specified by the FXML file path.
     *
     * @param fxmlPath The path to the FXML file of the screen to navigate to.
     * @param title    The title of the new screen.
     * @param node     The JavaFX node used to get the stage for navigation.
     */
    public static void navigateTo(String fxmlPath, String title, Node node) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
