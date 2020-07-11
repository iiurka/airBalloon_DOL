package com.dol.ui.elements;

import com.dol.ui.exceptions.FileNotFoundException;
import com.dol.ui.util.ImagePanelObserver;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ToolBar extends JToolBar {

    enum Action {
        NOTHING,
        SET_A,
        SET_B,
    }

    private final int widthAction = 25;
    private final int heightAction = 25;

    public ImagePanelObserver observer = null;

    Action pressedAction = Action.NOTHING;

    public ToolBar() {

        add(new RunAction());
        add(new Separator());
        add(new SetPointAAction());
        add(new SetPointBAction());
        add(new Separator());
        add(new SpeedControllerAction());
        add(new Separator());
        add(new ParentRegionAction());

        setBorder(new BevelBorder(BevelBorder.RAISED));
        setFloatable(false);
    }

    public Action getPressedAction() {
        return pressedAction;
    }

    public void resetPressedAction() {
        pressedAction = Action.NOTHING;
    }

    class RunAction extends AbstractAction  {

        public RunAction() {
            BufferedImage image;
            try {
                image = ImageIO.read(new File("resources" + File.separator + "air-balloon.png"));
            } catch (IOException e) {
                throw new FileNotFoundException("File \"air-balloon.png\" not found");
            }

            ImageIcon icon = new ImageIcon(image.getScaledInstance(widthAction, heightAction, Image.SCALE_SMOOTH));
            putValue(AbstractAction.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            observer.run();
        }
    }

    class SetPointAAction extends AbstractAction {

        public SetPointAAction() {
            BufferedImage image;
            try {
                image = ImageIO.read(new File("resources" + File.separator + "A.png"));
            } catch (IOException e) {
                throw new FileNotFoundException("File \"A.png\" not found");
            }

            ImageIcon icon = new ImageIcon(image.getScaledInstance(widthAction, heightAction, Image.SCALE_SMOOTH));
            putValue(AbstractAction.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            pressedAction = Action.SET_A;
        }
    }

    class SetPointBAction extends AbstractAction {

        public SetPointBAction() {
            BufferedImage image;
            try {
                image = ImageIO.read(new File("resources" + File.separator + "B.png"));
            } catch (IOException e) {
                throw new FileNotFoundException("File \"B.png\" not found");
            }

            ImageIcon icon = new ImageIcon(image.getScaledInstance(widthAction, heightAction, Image.SCALE_SMOOTH));
            putValue(AbstractAction.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            pressedAction = Action.SET_B;
        }
    }

    class SpeedControllerAction extends AbstractAction {

        public SpeedControllerAction() {
            BufferedImage image;
            try {
                image = ImageIO.read(new File("resources" + File.separator + "speed.png"));
            } catch (IOException e) {
                throw new FileNotFoundException("File \"speed.png\" not found");
            }

            ImageIcon icon = new ImageIcon(image.getScaledInstance(widthAction, heightAction, Image.SCALE_SMOOTH));
            putValue(AbstractAction.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            observer.openSpeedControllerWindow();
        }
    }

    class ParentRegionAction extends AbstractAction {

        public ParentRegionAction() {
            BufferedImage image;
            try {
                image = ImageIO.read(new File("resources" + File.separator + "world icon.png"));
            } catch (IOException e) {
                throw new FileNotFoundException("File \"world icon.png\" not found");
            }

            ImageIcon icon = new ImageIcon(image.getScaledInstance(widthAction, heightAction, Image.SCALE_SMOOTH));
            putValue(AbstractAction.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            observer.setDefaultRegion();
        }
    }
}
