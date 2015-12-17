package com.nerdery.smartlocker.test;

import com.nerdery.smartlocker.test.gui.TestFrame;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            new TestFrame().setVisible(true);
        });
    }
}
