package com.oppahansi;

import com.oppahansi.logic.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXML/Welcome.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("WoW Copy UI");
        stage.getIcons().add(new Image("/img/wow.png"));
        stage.show();

        // Pretty sure there is a better way, but that is a story for another day.
        // E.g. using a singleton.. etc
        Utils.stage = stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    // TODO: There is duplicate code in the services and controllers - get rid of it
}
