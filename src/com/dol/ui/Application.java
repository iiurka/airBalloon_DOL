package com.dol.ui;

import com.dol.ui.elements.ContextMenu;
import com.dol.ui.elements.ImagePanel;

import javax.swing.*;
import java.awt.*;


public class Application extends JFrame {

    private JPanel mainPanel;

    Application() {

        super("AirBalloon");

        final int width = 1777;
        final int height = 1000;
        ImagePanel mapPanel = new ImagePanel(width, height);
        add(ContextMenu.getPopupMenu(this));

        setContentPane(mainPanel);
        add(mapPanel, BorderLayout.CENTER);

        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        new Application();
    }
}
