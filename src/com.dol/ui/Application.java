package com.dol.ui;

import com.dol.ui.elements.ImagePanel;
import com.dol.ui.elements.ToolBar;
import com.dol.ui.exceptions.FileNotFoundException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.File;


public class Application extends JFrame {

    final int width = 1280;
    final int height = 900;

    Application() {

        super("DOL Balloon");
        setSize(width, height);
        setIconImage(new ImageIcon("resources" + File.separator + "icon.png").getImage());

        JPanel mainPanel = new JPanel(new BorderLayout());
        ImagePanel mapPanel;
        ToolBar toolBar;
        JPanel statusBar = new JPanel();

        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusBar.setPreferredSize(new Dimension(width, 16));
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        JLabel statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusBar.add(statusLabel);

        try {
            toolBar = new ToolBar();
            mapPanel = new ImagePanel(this, toolBar, statusLabel);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return;
        }

        rootPane.setContentPane(mainPanel);
        add(toolBar, BorderLayout.NORTH);
        add(mapPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

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
