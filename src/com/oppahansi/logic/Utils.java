package com.oppahansi.logic;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sun.xml.internal.ws.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Utils {
    private static JFileChooser folderChooser;

    public static File sourceFolder;
    public static File destinationFolder;

    public static String templateAccountName = "MYACCOUNTNAME";
    public static String templateRealmName = "MYREALMNAME";
    public static String templateCharName = "MYCHARNAME";

    public static String foldersNotSetUp = "Folders were not properly set up. Please select folders and try again.";
    public static String invalidAccFolder = "Invalid account folder selected. Please select the correct WTF/Account folder.";
    public static String invalidTemplateFolder = "Invalid template folder selected. Please select MYACCOUNTNAME template folder.";
    public static String errorCopying = "There was an error while copying files. Read and write permissions required.";
    public static String errorRenaming = "There was an error while renaming variables in files. Read and write permissions required.";
    public static String uiSetUpForChar = "New UI for character - %s - has been set up.";

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

        folderChooser.setDialogTitle(title);
        if (folderChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            if (source)
                sourceFolder = folderChooser.getSelectedFile();
            else
                destinationFolder = folderChooser.getSelectedFile();

            return folderChooser.getSelectedFile().getAbsolutePath();
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

        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));

            String line = reader.readLine();
            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }

            String newContent = oldContent.replaceAll(oldString1, newString1);
            newContent = newContent.replace(oldString2, newString2);

            writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private static void setupFileIO() {
        folderChooser = new JFileChooser();
        folderChooser.setCurrentDirectory(new java.io.File("."));
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderChooser.setAcceptAllFileFilterUsed(false);
    }



}
