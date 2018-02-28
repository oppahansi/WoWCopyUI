package com.oppahansi.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.oppahansi.logic.SetupUITemplateService;
import com.oppahansi.logic.Utils;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class TemplateController {

    public JFXButton templateFolderButton;
    public JFXTextField accountName;
    public JFXTextField realmName;
    public JFXTextField charName;
    public JFXButton setupUIButton;
    public JFXTextField templatePath;
    public JFXProgressBar progressBar;
    public JFXTextField notifications;
    public JFXButton accountFolderButton;
    public JFXTextField accountPath;
    public JFXButton backButton;
    public AnchorPane anchorPane;

    @FXML
    public void onTemplateFolderAction(ActionEvent event) {
        templatePath.setText(Utils.selectFolder("Select Template Folder..", true));
    }

    @FXML
    void onAccountFolderAction(ActionEvent event) {
        accountPath.setText(Utils.selectFolder("Select WTF/Account Folder..", false));
    }

    @FXML
    void onSetupUIAction(ActionEvent event) {
        SetupUITemplateService service = new SetupUITemplateService(accountName.getText(), realmName.getText(), charName.getText());
        notifications.textProperty().bind(service.messageProperty());
        progressBar.progressProperty().bind(service.progressProperty());
        service.start();
    }

    @FXML
    public void onBackAction(ActionEvent event) {
        try {
            AnchorPane welcomePane = FXMLLoader.load(getClass().getResource("/com/oppahansi/FXML/Welcome.fxml"));
            anchorPane.getChildren().setAll(welcomePane);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        ArrayList<JFXTextField> textFieldList = new ArrayList<>();
        textFieldList.add(accountName);
        textFieldList.add(realmName);
        textFieldList.add(charName);

        resetElements(textFieldList);

        setupUIButton.disableProperty()
                .bind(Bindings.isEmpty(accountName.textProperty())
                        .or(Bindings.isEmpty(realmName.textProperty()))
                        .or(Bindings.isEmpty(charName.textProperty()))
                        .or(Bindings.isEmpty(templatePath.textProperty()))
                        .or(Bindings.isEmpty(accountPath.textProperty())));

        Utils.addValidators(textFieldList);
    }

    private void resetElements(ArrayList<JFXTextField> textFieldList) {
        for (JFXTextField textField : textFieldList) {
            textField.setText("");
        }

        notifications.setText("");
        templatePath.setText("");
        accountPath.setText("");

        progressBar.setProgress(0);
    }


}
