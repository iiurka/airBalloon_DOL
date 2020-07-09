package com.dol.ui.elements;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ToolBar extends JToolBar {

    final int widthAction = 25;
    final int heightAction = 25;

    private boolean pressedOnA = false;
    private boolean pressedOnB = false;

    public ToolBar() {

        add(new RunAction());
        add(new SetPointAAction());
        add(new SetPointBAction());
        setBorder(new BevelBorder(BevelBorder.RAISED));
        setFloatable(false);
    }

    class RunAction extends AbstractAction  {

        public RunAction() {
            BufferedImage img;
            try {
                img = ImageIO.read(new File("resources/air-balloon.png"));
            } catch (IOException ignored) {
                return;
            }

            ImageIcon icon = new ImageIcon(img.getScaledInstance(widthAction, heightAction, Image.SCALE_SMOOTH));
            putValue(AbstractAction.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: добавить вызов метода полёта шара из бэкенда
        }
    }

    class SetPointAAction extends AbstractAction {

        public SetPointAAction() {
            BufferedImage img;
            try {
                img = ImageIO.read(new File("resources/A.png"));
            } catch (IOException ignored) {
                return;
            }

            ImageIcon icon = new ImageIcon(img.getScaledInstance(widthAction, heightAction, Image.SCALE_SMOOTH));
            putValue(AbstractAction.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            pressedOnA = true;
            pressedOnB = false;
        }
    }

    class SetPointBAction extends AbstractAction {

        public SetPointBAction() {
            BufferedImage img;
            try {
                img = ImageIO.read(new File("resources/B.png"));
            } catch (IOException ignored) {
                return;
            }

            ImageIcon icon = new ImageIcon(img.getScaledInstance(widthAction, heightAction, Image.SCALE_SMOOTH));
            putValue(AbstractAction.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            pressedOnA = false;
            pressedOnB = true;
        }
    }
}
