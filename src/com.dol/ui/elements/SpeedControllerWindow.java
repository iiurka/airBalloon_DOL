package com.dol.ui.elements;

import javax.swing.*;
import java.awt.*;

public class SpeedControllerWindow extends JDialog {

    private static int currentSpeed = 0;
    private final JSlider slider;

    SpeedControllerWindow(JFrame owner) {

        super(owner, "Speed controller", true);

        setLayout(new BorderLayout());
        setSize(500, 150);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        slider = new JSlider();
        add(slider, BorderLayout.CENTER);

        slider.setValue(currentSpeed);
        slider.setMaximum(240);
        slider.setMajorTickSpacing(24);
        slider.setMinorTickSpacing(12);
        slider.setLabelTable(slider.createStandardLabels(24));
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);

        slider.addChangeListener(e -> currentSpeed = slider.getValue());

        JButton buttonOk = new JButton("Ok");
        add(buttonOk, BorderLayout.SOUTH);
        buttonOk.addActionListener(e -> dispose());
    }

    static public int getCurrentSpeed() {
        return currentSpeed;
    }
}
