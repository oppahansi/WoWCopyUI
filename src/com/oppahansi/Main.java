package com.oppahansi;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        AccountSetup frame = new AccountSetup();
        frame.setTitle("WoW Copy UI");
        frame.setVisible(true);
        frame.setBounds(10, 10, 400, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }
}
