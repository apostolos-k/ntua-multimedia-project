module org.elibrary.ntuamultimediaproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.elibrary.ntuamultimediaproject to javafx.fxml;
    exports org.elibrary.ntuamultimediaproject;
    exports org.elibrary.ntuamultimediaproject.controllers;
    opens org.elibrary.ntuamultimediaproject.controllers to javafx.fxml;
}