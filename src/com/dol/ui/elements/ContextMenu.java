package com.dol.ui.elements;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContextMenu extends MouseAdapter {

    JFrame frame;
    JPopupMenu popupMenu = new JPopupMenu();

    public ContextMenu(JFrame frame) {

        this.frame = frame;

        JMenuItem itemStart = new JMenuItem("Start flight");
        JMenuItem itemSpeed = new JMenuItem("Change air balloon speed");

        //TODO: добавить вызов метода полёта шара из бэкенда
        itemStart.addActionListener(e -> {});
        itemSpeed.addActionListener(e -> new SpeedControllerWindow(frame).setVisible(true));

        frame.addMouseListener(this);

        popupMenu.add(itemStart);
        popupMenu.add(itemSpeed);
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    public static JPopupMenu getPopupMenu(JFrame frame) {
        return new ContextMenu(frame).popupMenu;
    }
}
