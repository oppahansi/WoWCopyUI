package com.oppahansi.logic;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sun.xml.internal.ws.util.StringUtils;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class Utils {
    public static Stage stage;

    public static File sourceFolder;
    public static File destinationFolder;

    public static String templateAccountName = "MYACCOUNTNAME";
    public static String templateRealmName = "MYREALMNAME";
    public static String templateCharName = "MYCHARNAME";
    public static String savedVariablesFodler = "SavedVariables";

    public static String foldersNotSetUp = "Folders were not properly set up. Please select folders and try again.";
    public static String invalidAccFolder = "Invalid account folder selected. Please select the correct WTF/Account folder.";
    public static String invalidTemplateFolder = "Invalid template folder selected. Please select MYACCOUNTNAME template folder.";
    public static String errorCopying = "There was an error while copying files. Read and write permissions required.";
    public static String errorRenaming = "There was an error while renaming variables in files. Read and write permissions required.";
    public static String uiSetUpForChar = "New UI for character - %s - has been set up.";

    private static DirectoryChooser folderChooser;

    public static void fixInputString(JFXTextField textField, boolean capitalize) {
        if (textField == null)
            return;

        if (capitalize)
            textField.setText(StringUtils.capitalize(textField.getText()));
        else
            textField.setText(textField.getText().toUpperCase());
    }

    public static void addValidators(ArrayList<JFXTextField> textFieldList) {
        for (JFXTextField textField : textFieldList) {
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Required field.");

            textField.getValidators().add(validator);
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                textField.validate();

                if (textField.getId().toLowerCase().contains("acc"))
                    fixInputString(textField, false);
                else
                    fixInputString(textField, true);
            });
        }
    }

    public static String selectFolder(String title, boolean source) {
        if (folderChooser == null) setupFileIO();

        folderChooser.setTitle(title);
        File selectedFolder = folderChooser.showDialog(stage);

        if (selectedFolder != null) {
            if (source)
                sourceFolder = selectedFolder;
            else
                destinationFolder = selectedFolder;

            return selectedFolder.getAbsolutePath();
        } else {
            if (source)
                sourceFolder = null;
            else
                destinationFolder = null;

            return "";
        }
    }

    public static void modifyFile(String filePath, String oldString1, String newString1, String oldString2, String newString2) {
        File fileToBeModified = new File(filePath);

        if (!fileToBeModified.isFile())
            return;

        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            String line = reader.readLine();
            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }

            String newContent = oldContent.replaceAll(oldString1, newString1);
            newContent = newContent.replace(oldString2, newString2);

            writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setupFileIO() {
        folderChooser = new DirectoryChooser();
        folderChooser.setInitialDirectory(new java.io.File("."));
    }
}
