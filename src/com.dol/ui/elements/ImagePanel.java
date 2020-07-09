package com.dol.ui.elements;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.dol.ui.exceptions.FileNotFoundException;
import com.dol.ui.util.GeoCoordinates;
import com.dol.ui.util.ImagePanelObserver;
import com.dol.ui.util.Point;

public class ImagePanel extends JPanel
                        implements MouseListener, MouseMotionListener {

    private final JFrame frame;
    private final JLabel statusBar;
    private final ContextMenu contextMenu;

    private final Image imageMap;

    private int widthMap;
    private int heightMap;

    Point A;
    Point B;

    public ImagePanel(JFrame frame, JLabel statusBar) throws FileNotFoundException {
        this.frame = frame;
        this.statusBar = statusBar;
        contextMenu = new ContextMenu(frame, new ImagePanelObserver(this));

        widthMap = frame.getWidth() - 16;
        heightMap = frame.getHeight() - 54;

        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (A.isWorth()) {
                    A.move(A.getX(), A.getY(), widthMap, heightMap);
                    paintPoint(A);
                }

                if (B.isWorth()) {
                    B.move(B.getX(), B.getY(), widthMap, heightMap);
                    paintPoint(B);
                }
            }
        });

        setComponentPopupMenu(contextMenu);
        add(getComponentPopupMenu());

        try {
            imageMap = ImageIO.read(new File("resources" + File.separator + "world map.jpg"));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"world map.jpg\" not found");
        }

        try {
            A = new Point(ImageIO.read(new File("resources" + File.separator + "point A.png")));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"point A.png\" not found");
        }

        try {
            B = new Point(ImageIO.read(new File("resources" + File.separator + "point B.png")));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"point B.png\" not found");
        }
    }

    protected void paintComponent(Graphics g) {
        //выравнивание до пропорций исходного изображения
        widthMap = frame.getWidth() - 16;
        heightMap = frame.getHeight() - 54;
        g.drawImage(imageMap, 0, 0, widthMap, heightMap, null);
    }

    private void paintPoint(Point point) {
        if (!point.isWorth()) return;

        getGraphics().drawImage(point.getImagePoint(), (int) (point.getX() - 12.5), point.getY() - 75, 50, 50, null);
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

        GeoCoordinates point = new GeoCoordinates(e.getX(), e.getY(), widthMap, heightMap);

        if (contextMenu.isPressedOnA()) {

            if (A.isWorth())
                clearMap();

            A.move(point);
            paintPoint(A);

        } else if (contextMenu.isPressedOnB()) {

            if (B.isWorth())
                clearMap();

            B.move(point);
            paintPoint(A);
            paintPoint(B);

        } else if (contextMenu.isPressedOnStart()) {
            A.deletePoint();
            B.deletePoint();
        }
    }

    public Point getPointA() {
        return A;
    }

    public Point getPointB() {
        return B;
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
        paintPoint(A);
        paintPoint(B);
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
        paintPoint(A);
        paintPoint(B);
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
        GeoCoordinates point = new GeoCoordinates(e.getX(), e.getY(), widthMap, heightMap);
        statusBar.setText(String.format("(%.2f; %.2f)", point.getLongitude(), point.getLatitude()));
    }
}