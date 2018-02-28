package com.oppahansi.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import com.jfoenix.validation.RequiredFieldValidator;
import com.oppahansi.logic.SetupUIService;
import com.oppahansi.logic.SetupUITemplateService;
import com.oppahansi.logic.Utils;
import com.sun.org.glassfish.external.statistics.annotations.Reset;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ExistingController {

    public JFXTextField newCharName;
    public JFXButton oldAccountFolderButton;
    public JFXTextField oldAccountName;
    public JFXTextField oldRealmName;
    public JFXTextField oldCharName;
    public JFXButton setupUIButton;
    public JFXTextField oldAccountPath;
    public JFXProgressBar progressBar;
    public JFXTextField notifications;
    public JFXTextField newAccountName;
    public JFXTextField newRealmName;
    public JFXButton newAccountFolderButton;
    public JFXTextField newAccountPath;
    public JFXButton backButton;
    public AnchorPane anchorPane;

    @FXML
    public void onOldAccountFolderAction(ActionEvent event) {
        oldAccountPath.setText(Utils.selectFolder("Select old account folder..", true));
    }

    @FXML
    public void onNewAccountFolderAction(ActionEvent event) {
        newAccountPath.setText(Utils.selectFolder("Select WTF/Account folder..", false));
    }

    @FXML
    public void onSetupUIAction(ActionEvent event) {
        SetupUIService service = new SetupUIService(oldAccountName.getText(), oldRealmName.getText(), oldCharName.getText(), newAccountName.getText(), newRealmName.getText(), newCharName.getText());
        notifications.textProperty().bind(service.messageProperty());
        progressBar.progressProperty().bind(service.progressProperty());
        service.start();
    }

    @FXML
    public void onBackAction(ActionEvent event) {
        try {
            AnchorPane welcomePane = FXMLLoader.load(getClass().getResource("/com/oppahansi/FXML/Welcome.fxml"));
            anchorPane.getChildren().setAll(welcomePane);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        ArrayList<JFXTextField> textFieldList = new ArrayList<>();
        textFieldList.add(oldAccountName);
        textFieldList.add(oldRealmName);
        textFieldList.add(oldCharName);
        textFieldList.add(newAccountName);
        textFieldList.add(newRealmName);
        textFieldList.add(newCharName);

        resetElements(textFieldList);

        setupUIButton.disableProperty()
                .bind(Bindings.isEmpty(oldAccountName.textProperty())
                        .or(Bindings.isEmpty(oldRealmName.textProperty()))
                        .or(Bindings.isEmpty(oldCharName.textProperty()))
                        .or(Bindings.isEmpty(newAccountName.textProperty()))
                        .or(Bindings.isEmpty(newRealmName.textProperty()))
                        .or(Bindings.isEmpty(newCharName.textProperty()))
                        .or(Bindings.isEmpty(oldAccountPath.textProperty()))
                        .or(Bindings.isEmpty(newAccountPath.textProperty())));

        Utils.addValidators(textFieldList);
    }

    private void resetElements(ArrayList<JFXTextField> textFieldList) {
        for (JFXTextField textField : textFieldList) {
            textField.setText("");
        }

        oldAccountPath.setText("");
        newAccountPath.setText("");
        notifications.setText("");

        progressBar.setProgress(0);
    }
}

