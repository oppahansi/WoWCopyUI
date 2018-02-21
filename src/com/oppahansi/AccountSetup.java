package com.oppahansi;

import com.sun.xml.internal.ws.util.StringUtils;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class AccountSetup extends JFrame implements ActionListener {

    private Container container = getContentPane();

    private JLabel accountNameLabel = new JLabel("NEW ACCOUNTNAME");
    private JLabel realmNameLabel = new JLabel("NEW Realm name");
    private JLabel charNameLabel = new JLabel("NEW Char name");
    private JLabel oldAccountNameLabel = new JLabel("OLD ACCOUNTNAME");
    private JLabel oldRealmNameLabel = new JLabel("OLD Realm name");
    private JLabel oldCharNameLabel = new JLabel("OLD Char name");

    private JTextField accountNameField = new JTextField();
    private JTextField realmNameField = new JTextField();
    private JTextField charNameField = new JTextField();
    private JTextField oldAccountNameField = new JTextField();
    private JTextField oldRealmNameField = new JTextField();
    private JTextField oldCharNameField = new JTextField();

    private JLabel notificationText = new JLabel("");

    private JButton createNewAccountButton = new JButton("Copy UI");

    private JCheckBox usingExistingAccount = new JCheckBox("Using existing UI?");

    private JFileChooser folderChooser;
    private File templateFolder;
    private File accountFolder;

    private ArrayList<File> files = new ArrayList<>();

    private String templateRealm = "MYREALMNAME";
    private String templateChar = "MYCHARNAME";
    private String realmName = "";
    private String charName = "";
    private String oldRealmName = "";
    private String oldCharName = "";
    private String accountName = "";
    private String oldAccountName = "";

    AccountSetup() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
    }

    private void setLayoutManager() {
        container.setLayout(null);
    }

    private void setLocationAndSize() {
        usingExistingAccount.setBounds(50, 25, 200, 30);

        accountNameLabel.setBounds(50, 70, 150, 30);
        realmNameLabel.setBounds(50, 110, 150, 30);
        charNameLabel.setBounds(50, 150, 150, 30);

        accountNameField.setBounds(200, 70, 150, 30);
        realmNameField.setBounds(200, 110, 150, 30);
        charNameField.setBounds(200, 150, 150, 30);

        oldAccountNameLabel.setBounds(50, 190, 150, 30);
        oldRealmNameLabel.setBounds(50, 230, 150, 30);
        oldCharNameLabel.setBounds(50, 270, 150, 30);

        oldAccountNameField.setBounds(200, 190, 150, 30);
        oldRealmNameField.setBounds(200, 230, 150, 30);
        oldCharNameField.setBounds(200, 270, 150, 30);

        oldAccountNameLabel.setEnabled(false);
        oldRealmNameLabel.setEnabled(false);
        oldCharNameLabel.setEnabled(false);

        oldAccountNameField.setEnabled(false);
        oldRealmNameField.setEnabled(false);
        oldCharNameField.setEnabled(false);

        createNewAccountButton.setBounds(50, 350, 150, 30);

        notificationText.setBounds(50, 375, 300, 30);
        notificationText.setForeground(Color.RED);
    }

    private void addComponentsToContainer() {
        container.add(accountNameLabel);
        container.add(realmNameLabel);
        container.add(charNameLabel);
        container.add(accountNameField);
        container.add(realmNameField);
        container.add(charNameField);

        container.add(oldAccountNameLabel);
        container.add(oldRealmNameLabel);
        container.add(oldCharNameLabel);
        container.add(oldAccountNameField);
        container.add(oldRealmNameField);
        container.add(oldCharNameField);

        container.add(usingExistingAccount);
        container.add(createNewAccountButton);

        container.add(notificationText);
    }

    private void addActionEvent() {
        createNewAccountButton.addActionListener(this);
        usingExistingAccount.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createNewAccountButton) {
            accountName = accountNameField.getText();
            realmName = realmNameField.getText();
            charName = charNameField.getText();
            oldAccountName = oldAccountNameField.getText();
            oldRealmName = oldRealmNameField.getText();
            oldCharName = oldCharNameField.getText();

            fixStrings();

            if (accountName == null || accountName.length() == 0) {
                updateTextNotification("Account name cannot be empty.", Color.RED);
                return;
            } else if (realmName == null || realmName.length() == 0) {
                updateTextNotification("Realm cannot be empty.", Color.RED);
                return;
            } else if (charName == null || charName.length() == 0) {
                updateTextNotification("Char name cannot be empty.", Color.RED);
                return;
            }
            else if (usingExistingAccount.isSelected()) {
                if (oldAccountName == null || oldAccountName.length() == 0) {
                    updateTextNotification("Old Account name cannot be empty.", Color.RED);
                    return;
                } else if (oldRealmName == null || oldRealmName.length() == 0) {
                    updateTextNotification("Old Realm cannot be empty.", Color.RED);
                    return;
                } else if (oldCharName == null || oldCharName.length() == 0) {
                    updateTextNotification("Old Char name cannot be empty.", Color.RED);
                    return;
                }
            }

            createNewAccount(accountName);

        } else if (e.getSource() == usingExistingAccount) {
            oldAccountNameLabel.setEnabled(usingExistingAccount.isSelected());
            oldRealmNameLabel.setEnabled(usingExistingAccount.isSelected());
            oldCharNameLabel.setEnabled(usingExistingAccount.isSelected());
            oldAccountNameField.setEnabled(usingExistingAccount.isSelected());
            oldRealmNameField.setEnabled(usingExistingAccount.isSelected());
            oldCharNameField.setEnabled(usingExistingAccount.isSelected());
        }
    }

    private void fixStrings()
    {
        accountName = accountName.toUpperCase();
        realmName = StringUtils.capitalize(realmName.toLowerCase());
        charName =  StringUtils.capitalize(charName.toLowerCase());
        oldAccountName = oldAccountName.toUpperCase();
        oldRealmName = StringUtils.capitalize(oldRealmName.toLowerCase());
        oldCharName = StringUtils.capitalize(oldCharName.toLowerCase());
    }

    private void createNewAccount(String account) {
        setupFileIO();

        if (!selectFolders(account)) {
            if (templateFolder == null || accountFolder == null)
                updateTextNotification("Folders could not be initialized. Select correct folders or provide correct account, realm and char names.", Color.RED);
            else {
                if (copyFiles())
                {
                    try {
                        renameSubFolders();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        return;
                    }

                    files = addFilesToList(accountFolder.getAbsolutePath());

                    if (files.size() == 0)
                    {
                        updateTextNotification("Could not find files in account folders.", Color.RED);
                        return;
                    }

                    renameVariablesInFiles();
                }
            }
        } else
            updateTextNotification("Folders could not be initialized. Select correct folders or provide correct account, realm and char names.", Color.RED);

    }

    private boolean selectFolders(String account) {
        if (folderChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            templateFolder = folderChooser.getSelectedFile();

            folderChooser.setDialogTitle("Select destination WTF/Account folder");

            if (folderChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                accountFolder = new File(folderChooser.getSelectedFile()+ "\\" + account);

            return templateFolder.exists() && accountFolder.exists();
        }

        return false;
    }

    private void setupFileIO() {
        folderChooser = new JFileChooser();
        folderChooser.setCurrentDirectory(new java.io.File("."));
        folderChooser.setDialogTitle(usingExistingAccount.isSelected() ? "Select Existing Account Folder." : "Select VG UI Template Folder");
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderChooser.setAcceptAllFileFilterUsed(false);
    }

    private boolean copyFiles() {
        accountFolder = new File(usingExistingAccount.isSelected() ? accountFolder.getParentFile().getAbsolutePath() + "\\" + accountName : accountFolder.getAbsolutePath());

        try {
            FileUtils.copyDirectory(templateFolder, accountFolder);
            return true;
        } catch (IOException e1) {
            updateTextNotification("Copying files failed..", Color.RED);
            e1.printStackTrace();
            return false;
        }
    }

    private void renameSubFolders() throws IOException {
        String realm = usingExistingAccount.isSelected() ? oldRealmName : templateRealm;
        String character = usingExistingAccount.isSelected() ? oldCharName : templateChar;

        String realmPath = usingExistingAccount.isSelected() ?
                accountFolder.getAbsolutePath() + "\\" + oldRealmName :
                accountFolder.getAbsolutePath() + "\\" + realm;

        String charPath = usingExistingAccount.isSelected() ?
                accountFolder.getAbsolutePath() + "\\" + oldRealmName + "\\" + character :
                accountFolder.getAbsolutePath() + "\\" + realmName +"\\" + character;

        File dir = new File(realmPath);
        File newDir = new File(accountFolder.getAbsolutePath() + "\\" + realmName);

        Files.move(dir.toPath(), newDir.toPath());

        dir = new File(charPath);
        newDir = new File(accountFolder.getAbsolutePath() + "\\" + realmName +"\\" + charName);

        Files.move(dir.toPath(), newDir.toPath());
    }

    private ArrayList<File> addFilesToList(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();

        if (fList == null)
            return new ArrayList<>();

        ArrayList<File> resultList = new ArrayList<>(Arrays.asList(fList));

        for (File file : fList) {
            if (file.isDirectory()) {
                resultList.addAll(addFilesToList(file.getAbsolutePath()));
            }
        }

        return resultList;
    }

    private void renameVariablesInFiles() {
        String replaceRealm = usingExistingAccount.isSelected() ? oldRealmName : templateRealm;
        String replaceChar = usingExistingAccount.isSelected() ? oldCharName : templateChar;

        for (File currentFile : files)
        {
            if (!currentFile.isFile())
                continue;

            modifyFile(currentFile.getAbsolutePath(), replaceRealm, realmName, replaceChar, charName);
        }

        updateTextNotification("Account has been set up.", Color.GREEN);
    }

    void modifyFile(String filePath, String oldString1, String newString1, String oldString2, String newString2)
    {
        updateTextNotification("Modyfying " + filePath, Color.GREEN);

        File fileToBeModified = new File(filePath);
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

    private void updateTextNotification(String message, Color color)
    {
        notificationText.setForeground(color);
        notificationText.setText(message);
    }
}