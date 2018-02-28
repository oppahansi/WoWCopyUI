package com.oppahansi.logic;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;

public class SetupUIService extends Service {

    private String oldAccName;
    private String oldRealmName;
    private String oldCharName;
    private String newAccName;
    private String newRealmName;
    private String newCharName;
    private double progress;
    private double step;


    public SetupUIService(String oldAccName, String oldRealmName, String oldCharName, String newAccName, String newRealmName, String newCharName) {
        this.oldAccName = oldAccName;
        this.oldRealmName = oldRealmName;
        this.oldCharName = oldCharName;
        this.newAccName = newAccName;
        this.newRealmName = newRealmName;
        this.newCharName = newCharName;
        this.progress = 0.0;
        this.step = 0.0;
    }

    @Override
    protected Task createTask() {
        return new SetupUITask();
    }

    class SetupUITask extends Task {
        @Override
        protected Object call() {

            if (Utils.sourceFolder == null || Utils.destinationFolder == null)
            {
                updateMessage(Utils.foldersNotSetUp);
                updateProgress(0, 1);
                return null;
            }

            if (Utils.sourceFolder.getName().toLowerCase().compareTo(oldAccName.toLowerCase()) != 0)
            {
                updateMessage(Utils.invalidTemplateFolder);
                updateProgress(0, 1);
                return null;
            }

            if (Utils.destinationFolder.getName().toLowerCase().compareTo("account") != 0)
            {
                updateMessage(Utils.invalidAccFolder);
                updateProgress(0, 1);
                return null;
            }

            if (!copyFiles())
            {
                updateMessage(Utils.errorCopying);
                updateProgress(0, 1);
                return null;
            }

            if (!renameVariables())
            {
                updateMessage(Utils.errorRenaming);
                updateProgress(0, 1);
                return null;
            }

            updateMessage(String.format(Utils.uiSetUpForChar, newCharName));
            updateProgress(1, 1);

            CleanUp();

            return null;
        }

        private boolean copyFiles() {
            try {

                File accountFolder = new File(Utils.destinationFolder.getAbsolutePath() + "\\" + newAccName);
                if (!accountFolder.exists())
                    if (!accountFolder.mkdir()) return false;

                File realmFolder = new File(accountFolder.getAbsolutePath() + "\\" + newRealmName);
                if (!realmFolder.exists())
                    if (!realmFolder.mkdir()) return false;

                File charFolder = new File(realmFolder.getAbsolutePath() + "\\" + newCharName);
                if (!charFolder.exists())
                    if (!charFolder.mkdir()) return false;

                File templateSavedVariablesFolderAcc = new File(Utils.sourceFolder.getAbsolutePath() + "\\" + "SavedVariables");
                File templateCharFolder = new File(Utils.sourceFolder.getAbsolutePath() + "\\" + oldRealmName+ "\\" + oldCharName);
                File templateSavedVariablesFolderChar = new File(templateCharFolder + "\\" + "SavedVariables");
                File[] templateAccountFolderFiles = Utils.sourceFolder.listFiles(File::isFile);
                File[] templateCharFolderFiles = templateCharFolder.listFiles(File::isFile);
                File[] templateSavedVariablesAccFolderFiles = templateSavedVariablesFolderAcc.listFiles(File::isFile);
                File[] templateSavedVariablesCharFolderFiles = templateSavedVariablesFolderChar.listFiles(File::isFile);

                assert templateAccountFolderFiles != null;
                assert templateCharFolderFiles != null;
                assert templateSavedVariablesAccFolderFiles != null;
                assert templateSavedVariablesCharFolderFiles != null;

                step =  0.5 / (double)(templateAccountFolderFiles.length + templateCharFolderFiles.length + templateSavedVariablesAccFolderFiles.length + templateSavedVariablesCharFolderFiles.length);

                copyFiles(templateAccountFolderFiles, accountFolder.getAbsolutePath());
                copyFiles(templateSavedVariablesAccFolderFiles, templateSavedVariablesFolderAcc.getAbsolutePath());
                copyFiles(templateCharFolderFiles, charFolder.getAbsolutePath());
                copyFiles(templateSavedVariablesCharFolderFiles, charFolder.getAbsolutePath() + "\\" + "SavedVariables");

                return true;
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }
        }

        private void copyFiles(File[] files, String destinationPath) throws IOException {
            if (files != null) {
                for (File file : files) {
                    updateMessage("Copying: " + file.getName());
                    progress += step;
                    updateProgress(progress, 1);

                    File newFile = new File(destinationPath + "\\" + file.getName());

                    if (newFile.exists())
                        continue;

                    FileUtils.copyFile(file, newFile);
                }
            }
        }

        private boolean renameVariables() {

            File newAccountFolder = new File(Utils.destinationFolder.getAbsolutePath() + "\\" + newAccName);
            if (!newAccountFolder.exists())
                return false;

            File[] accountFolderFiles = newAccountFolder.listFiles(File::isFile);

            File savedVariablesFolderAcc = new File(newAccountFolder.getAbsolutePath() + "\\" + "SavedVariables");
            if (!savedVariablesFolderAcc.exists())
                return false;

            File[] savedVariablesAccFiles = savedVariablesFolderAcc.listFiles(File::isFile);

            File charFolder = new File(newAccountFolder.getAbsolutePath() + "\\" + newRealmName + "\\" + newCharName);
            if (!charFolder.exists())
                return false;

            File[] charFolderFiles = charFolder.listFiles(File::isFile);

            File savedVariablesFolderChar = new File(charFolder.getAbsolutePath() + "\\" + "SavedVariables");
            if (!savedVariablesFolderChar.exists())
                return false;

            File[] savedVariablesCharFiles = savedVariablesFolderChar.listFiles(File::isFile);

            File[] result;

            if (oldAccName.toLowerCase().compareTo(newAccName.toLowerCase()) == 0)
            {
                result = ArrayUtils.addAll(charFolderFiles, savedVariablesFolderChar);
            } else {
                File[] mergedAccountFolderFiles = ArrayUtils.addAll(accountFolderFiles, savedVariablesAccFiles);
                File[] mergedCharFolderFiles = ArrayUtils.addAll(charFolderFiles, savedVariablesCharFiles);

                result = ArrayUtils.addAll(mergedAccountFolderFiles, mergedCharFolderFiles);
            }

            progress = 0.50;
            step = 0.50 / result.length;

            for (File file : result)
            {
                Utils.modifyFile(file.getAbsolutePath(), oldRealmName, newRealmName, oldCharName, newCharName);
                progress += step;
                updateMessage("Updating: " + file.getName());
                updateProgress(progress, 1);
            }

            return true;
        }

        private void CleanUp()
        {
            Utils.sourceFolder = null;
            Utils.destinationFolder = null;
            progress = 0;
            step = 0;
        }
    }
}
