package org.elibrary.ntuamultimediaproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.elibrary.ntuamultimediaproject.utils.DatabaseManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager.deserializeDataOnStart();

        Parent loginRoot = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        Scene loginScene = new Scene(loginRoot);

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("eLibrary - Sign in");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DatabaseManager.setLoggedInUsername(null);
        DatabaseManager.serializeDataOnExit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
