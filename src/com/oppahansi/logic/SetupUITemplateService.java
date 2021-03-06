package com.oppahansi.logic;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;

public class SetupUITemplateService extends Service {

    private String accName;
    private String realmName;
    private String charName;
    private double progress;
    private double step;

    public SetupUITemplateService(String accName, String realmName, String charName) {
        this.accName = accName;
        this.realmName = realmName;
        this.charName = charName;
        this.progress = 0.0;
        this.step = 0.0;
    }

    @Override
    protected Task createTask() {
        return new SetupUITemplateTask();
    }

    class SetupUITemplateTask extends Task {
        @Override
        protected Object call() {

            if (Utils.sourceFolder == null || Utils.destinationFolder == null) {
                updateMessage(Utils.foldersNotSetUp);
                updateProgress(0, 1);
                return null;
            }

            if (Utils.sourceFolder.getName().toLowerCase().compareTo(Utils.templateAccountName.toLowerCase()) != 0) {
                updateMessage(Utils.invalidTemplateFolder);
                updateProgress(0, 1);
                return null;
            }

            if (Utils.destinationFolder.getName().toLowerCase().compareTo("account") != 0) {
                updateMessage(Utils.invalidAccFolder);
                updateProgress(0, 1);
                return null;
            }

            if (!copyFiles()) {
                updateMessage(Utils.errorCopying);
                updateProgress(0, 1);
                return null;
            }

            if (!renameVariables()) {
                updateMessage(Utils.errorRenaming);
                updateProgress(0, 1);
                return null;
            }

            updateMessage(String.format(Utils.uiSetUpForChar, charName));
            updateProgress(1, 1);

            CleanUp();

            return null;
        }

        private boolean copyFiles() {
            try {

                File accountFolder = new File(Utils.destinationFolder.getAbsolutePath() + File.separator + accName);
                if (!accountFolder.exists())
                    if (!accountFolder.mkdir()) return false;

                File savedVariablesFolderAcc = new File(accountFolder.getAbsolutePath() + File.separator + Utils.savedVariablesFodler);
                if (!savedVariablesFolderAcc.exists())
                    if (!savedVariablesFolderAcc.mkdir()) return false;

                File realmFolder = new File(accountFolder.getAbsolutePath() + File.separator + realmName);
                if (!realmFolder.exists())
                    if (!realmFolder.mkdir()) return false;

                File charFolder = new File(realmFolder.getAbsolutePath() + File.separator + charName);
                if (!charFolder.exists())
                    if (!charFolder.mkdir()) return false;

                File savedVariablesFolderChar = new File(charFolder.getAbsolutePath() + File.separator + Utils.savedVariablesFodler);
                if (!savedVariablesFolderChar.exists())
                    if (!savedVariablesFolderChar.mkdir()) return false;

                File templateSavedVariablesFolderAcc = new File(Utils.sourceFolder.getAbsolutePath() + File.separator + Utils.savedVariablesFodler);
                File templateCharFolder = new File(Utils.sourceFolder.getAbsolutePath() + File.separator + Utils.templateRealmName+ File.separator + Utils.templateCharName);
                File templateSavedVariablesFolderChar = new File(templateCharFolder + File.separator + Utils.savedVariablesFodler);
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
                copyFiles(templateSavedVariablesAccFolderFiles, savedVariablesFolderAcc.getAbsolutePath());
                copyFiles(templateCharFolderFiles, charFolder.getAbsolutePath());
                copyFiles(templateSavedVariablesCharFolderFiles, savedVariablesFolderChar.getAbsolutePath());

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

                    File newFile = new File(destinationPath + File.separator + file.getName());

                    if (newFile.exists())
                        continue;

                    FileUtils.copyFile(file, newFile);
                }
            }
        }

        private boolean renameVariables() {
            File newAccountFolder = new File(Utils.destinationFolder.getAbsolutePath() + File.separator + accName);
            if (!newAccountFolder.exists())
                return false;

            File[] accountFolderFiles = newAccountFolder.listFiles(File::isFile);

            File savedVariablesFolderAcc = new File(newAccountFolder.getAbsolutePath() + File.separator + Utils.savedVariablesFodler);
            if (!savedVariablesFolderAcc.exists())
                return false;

            File[] savedVariablesAccFiles = savedVariablesFolderAcc.listFiles(File::isFile);

            File charFolder = new File(newAccountFolder.getAbsolutePath() + File.separator + realmName + File.separator + charName);
            if (!charFolder.exists())
                return false;

            File[] charFolderFiles = charFolder.listFiles(File::isFile);

            File savedVariablesFolderChar = new File(charFolder.getAbsolutePath() + File.separator + Utils.savedVariablesFodler);
            if (!savedVariablesFolderChar.exists())
                return false;

            File[] savedVariablesCharFiles = savedVariablesFolderChar.listFiles(File::isFile);

            File[] mergedAccountFolderFiles = ArrayUtils.addAll(accountFolderFiles, savedVariablesAccFiles);
            File[] mergedCharFolderFiles = ArrayUtils.addAll(charFolderFiles, savedVariablesCharFiles);
            File[] result = ArrayUtils.addAll(mergedAccountFolderFiles, mergedCharFolderFiles);

            progress = 0.50;
            step = 0.50 / result.length;

            for (File file : result) {
                Utils.modifyFile(file.getAbsolutePath(), Utils.templateRealmName, realmName, Utils.templateCharName, charName);
                progress += step;
                updateMessage("Updating: " + file.getName());
                updateProgress(progress, 1);
            }

            return true;
        }

        private void CleanUp() {
            Utils.sourceFolder = null;
            Utils.destinationFolder = null;
            progress = 0;
            step = 0;
        }
    }
}
