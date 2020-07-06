package com.dol.ui.elements;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.dol.ui.exceptions.FileNotFoundException;
import com.dol.ui.util.GeoCoordinates;
import com.dol.ui.util.ImagePanelObserver;

public class ImagePanel extends JPanel
                        implements MouseListener, MouseMotionListener {

    private final JFrame frame;
    private final JLabel statusBar;
    private final ContextMenu contextMenu;

    private final Image imageMap;
    private final Image imagePointA;
    private final Image imagePointB;

    private GeoCoordinates pointA = new GeoCoordinates();
    private GeoCoordinates pointB = new GeoCoordinates();

    public ImagePanel(JFrame frame, JLabel statusBar) {
        this.frame = frame;
        this.statusBar = statusBar;
        contextMenu = new ContextMenu(frame, new ImagePanelObserver(this));

        addMouseListener(this);
        addMouseMotionListener(this);
        setComponentPopupMenu(contextMenu);
        add(getComponentPopupMenu());

        try {
            imageMap = ImageIO.read(new File("resources" + File.separator + "map4.jpg"));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"map4.jpg\" not found");
        }

        try {
            imagePointA = ImageIO.read(new File("resources" + File.separator + "point A.png"));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"point A.png\" not found");
        }

        try {
            imagePointB = ImageIO.read(new File("resources" + File.separator + "point B.png"));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"point B.png\" not found");
        }
    }

    protected void paintComponent(Graphics g) {
        //выравнивание до пропорций исходного изображения
        g.drawImage(imageMap, 0, 0, frame.getWidth()-15, frame.getHeight()-54, null);
    }

    private void paintPoint(int x, int y, Image imagePoint) {
        y -= 49;
        x -= 12.5;
        getGraphics().drawImage(imagePoint, x, y, 25, 50, null);
    }

    private void clearMap() {
        paintComponent(getGraphics());
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        GeoCoordinates point = new GeoCoordinates(e.getX(), e.getY(), frame.getWidth(), frame.getHeight());

        if (contextMenu.isPressedOnA()) {

            if (pointA != null)
                clearMap();

            pointA = point;

            paintPoint(e.getX(), e.getY(), imagePointA);

        } else if (contextMenu.isPressedOnB()) {

            if (pointB != null)
                clearMap();

            pointB = point;

            paintPoint(pointA.getCartesianCoords()[0], pointA.getCartesianCoords()[1], imagePointA);
            paintPoint(e.getX(), e.getY(), imagePointB);

        } else if (contextMenu.isPressedOnStart()) {
            pointA = null;
            pointB = null;
        }
    }

    public GeoCoordinates getPointA() {
        return pointA;
    }

    public GeoCoordinates getPointB() {
        return pointB;
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  {@code MOUSE_DRAGGED} events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * {@code MOUSE_DRAGGED} events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {}

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        GeoCoordinates point = new GeoCoordinates(e.getX(), e.getY(), frame.getWidth(), frame.getHeight());
        statusBar.setText(String.format("(%.2f; %.2f)", point.getGeographicCoords()[0], point.getGeographicCoords()[1]));
    }
}