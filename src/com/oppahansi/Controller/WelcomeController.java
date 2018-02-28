package com.oppahansi.Controller;
/**
 * Sample Skeleton for 'Welcome.fxml' Controller Class
 */

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class WelcomeController {

    public AnchorPane anchorPane;
    public JFXButton fromTemplateButton;
    public JFXButton fromExistingButton;
    public JFXButton makeTemplateButton;

    @FXML
    void onFromExistingClicked(MouseEvent event) {
        try {
            AnchorPane existingPane = FXMLLoader.load(getClass().getResource("/com/oppahansi/FXML/Existing.fxml"));
            anchorPane.getChildren().setAll(existingPane);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onFromTemplateClicked(MouseEvent event) {
        try {
            AnchorPane fromTemplatePate = FXMLLoader.load(getClass().getResource("/com/oppahansi/FXML/Template.fxml"));
            anchorPane.getChildren().setAll(fromTemplatePate);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onMakeTemplateClicked(MouseEvent event) {
        try {
            AnchorPane makeTemplatePane = FXMLLoader.load(getClass().getResource("/com/oppahansi/FXML/MakeTemplate.fxml"));
            anchorPane.getChildren().setAll(makeTemplatePane);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
