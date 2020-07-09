package com.dol.ui.elements;

import com.dol.ui.util.ImagePanelObserver;

import javax.swing.*;

public class ContextMenu extends JPopupMenu {

    private boolean pressedOnA = false;
    private boolean pressedOnB = false;
    private boolean pressedOnStart = false;

    public ContextMenu(JFrame frame, ImagePanelObserver imageObserver) {

        JMenuItem itemStart = new JMenuItem("Start flight");
        JMenuItem itemSpeed = new JMenuItem("Change air balloon speed");
        JMenuItem itemSetPoint = new JMenuItem("Set point A");

        //TODO: добавить вызов метода полёта шара из бэкенда
        itemStart.addActionListener(e -> pressedOnStart = true);
        itemSpeed.addActionListener(e -> new SpeedControllerWindow(frame).setVisible(true));

        itemSetPoint.addActionListener(e -> {
            if (!pressedOnA && !pressedOnB) {
                pressedOnA = true;
                itemSetPoint.setText("Set point B");
            } else if (pressedOnA) {

                if (imageObserver.getPointA().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select point A", "Warning!", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                pressedOnA = false;
                pressedOnB = true;
                itemSetPoint.setText("Done");
            } else {

                if (imageObserver.getPointB().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Select point B", "Warning!", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                pressedOnB = false;
                itemSetPoint.setText("Set point A");
            }
        });

        add(itemStart);
        add(itemSetPoint);
        add(new Separator());
        add(itemSpeed);
    }

    public boolean isPressedOnA() {
        return pressedOnA;
    }

    public boolean isPressedOnB() {
        return pressedOnB;
    }

    public boolean isPressedOnStart() {
        return pressedOnStart;
    }
}
