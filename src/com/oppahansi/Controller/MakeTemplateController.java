package com.oppahansi.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import com.oppahansi.logic.MakeTemplateService;
import com.oppahansi.logic.Utils;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class MakeTemplateController {

    public JFXButton oldAccountFolderButton;
    public JFXTextField realmName;
    public JFXTextField charName;
    public JFXButton createTemplateButton;
    public JFXTextField templatePath;
    public JFXProgressBar progressBar;
    public JFXTextField notifications;
    public JFXButton saveToButton;
    public JFXTextField savePath;
    public JFXButton backButton;
    public AnchorPane anchorPane;

    @FXML
    void onOldAccountFolderAction(ActionEvent event) {
        templatePath.setText(Utils.selectFolder("Select your player account Folder..", true));
    }

    @FXML
    void onSaveToAction(ActionEvent event) {
        savePath.setText(Utils.selectFolder("Save to..", false));
    }

    @FXML
    void onCreateTemplateAction(ActionEvent event) {
        MakeTemplateService service = new MakeTemplateService(realmName.getText(), charName.getText());
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

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        ArrayList<JFXTextField> textFieldList = new ArrayList<>();
        textFieldList.add(realmName);
        textFieldList.add(charName);

        resetElements(textFieldList);

        createTemplateButton.disableProperty()
                .bind(Bindings.isEmpty(realmName.textProperty())
                        .or(Bindings.isEmpty(charName.textProperty()))
                        .or(Bindings.isEmpty(templatePath.textProperty()))
                        .or(Bindings.isEmpty(savePath.textProperty())));

        Utils.addValidators(textFieldList);
    }

    private void resetElements(ArrayList<JFXTextField> textFieldList) {
        for (JFXTextField textField : textFieldList) {
            textField.setText("");
        }

        notifications.setText("");
        templatePath.setText("");
        savePath.setText("");

        progressBar.setProgress(0);
    }
}
