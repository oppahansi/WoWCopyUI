package com.oppahansi;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        AccountSetup frame = new AccountSetup();
        frame.setTitle("WoW Copy UI");
        frame.setBounds(10, 10, 400, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
