package com.oppahansi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXML/Welcome.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("WoW Copy UI");
        stage.getIcons().add(new Image("/img/wow.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
