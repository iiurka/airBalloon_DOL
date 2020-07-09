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
import com.dol.ui.util.Region;

public class ImagePanel extends JPanel
        implements MouseListener, MouseMotionListener {

    private final JFrame frame;
    private final ToolBar toolBar;
    private final JLabel statusBar;

    private final Image worldMap;
    private final Image europeMap;
    private final Image WMEurope;
    private Image currMap;

    private Region region = Region.WORLD;

    private int widthMap;
    private int heightMap;

    Point A;
    Point B;

    public ImagePanel(JFrame frame, ToolBar toolBar, JLabel statusBar) throws FileNotFoundException {
        this.frame = frame;

        toolBar.observer = new ImagePanelObserver(this);
        this.toolBar = toolBar;
        this.statusBar = statusBar;

        //ContextMenu contextMenu = new ContextMenu(frame, new ImagePanelObserver(this));
        //setComponentPopupMenu(contextMenu);
        //add(getComponentPopupMenu());

        addMouseListener(this);
        addMouseMotionListener(this);

        try {
            worldMap = ImageIO.read(new File("resources" + File.separator + "world map.jpg"));
            currMap = worldMap;
        } catch (IOException e) {
            throw new FileNotFoundException("File \"world map.jpg\" not found");
        }

        try {
            europeMap = ImageIO.read(new File("resources" + File.separator + "europe map.jpg"));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"europe map.jpg\" not found");
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

        try {
            WMEurope = ImageIO.read(new File("resources" + File.separator + "WM with europe.jpg"));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"WM with europe.jpg\" not found");
        }
    }

    protected void paintComponent(Graphics g) {
        //выравнивание до пропорций исходного изображения
        widthMap = frame.getWidth() - 17;
        heightMap = frame.getHeight() - 91;
        g.drawImage(currMap, 0, 0, widthMap, heightMap, null);
    }

    private void paintPoint(Point point) {
        if (!point.isWorth()) return;
        getGraphics().drawImage(point.getImagePoint(), point.getX() - 13, point.getY() - 41, 26, 42, null);
    }

    private void clearMap() {
        paintComponent(getGraphics());
    }

    public Point getPointA() {
        return A;
    }

    public Point getPointB() {
        return B;
    }

    public void openSpeedControllerWindow() {
        new SpeedControllerWindow(frame).setVisible(true);
    }

    public void setWorldRegion() {
        region = Region.WORLD;
        currMap = worldMap;
        paintComponent(getGraphics());
    }

    public void run() {
        //TODO: добавить вызов метода полёта шара из бэкенда
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        GeoCoordinates point = new GeoCoordinates(e.getX(), e.getY(), widthMap, heightMap, region);

        if (region == Region.WORLD) {
            if (-25 < point.getLongitude() && point.getLongitude() < 50 &&
                    35 < point.getLatitude() && point.getLatitude() < 71) {
                region = Region.EUROPE;
                currMap = europeMap;

                toolBar.resetPressedAction();
                paintComponent(getGraphics());
            }
            return;
        }


        switch (toolBar.getPressedAction()) {

            case NOTHING:
                break;

            case SET_A:
                clearMap();

                A.move(point);
                paintPoint(A);
                paintPoint(B);
                break;

            case SET_B:
                clearMap();

                B.move(point);
                paintPoint(A);
                paintPoint(B);
                break;
        }

        toolBar.resetPressedAction();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //if (e.isPopupTrigger())
            //getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());

        //paintPoint(A);
        //paintPoint(B);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //if (e.isPopupTrigger())
            //getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());

        //paintPoint(A);
        //paintPoint(B);
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        GeoCoordinates point = new GeoCoordinates(e.getX(), e.getY(), widthMap, heightMap, region);


        if (region == Region.WORLD) {
            if (-25 < point.getLongitude() && point.getLongitude() < 50 &&
                    35 < point.getLatitude() && point.getLatitude() < 71 ) {
                if (!currMap.equals(WMEurope)) {
                    currMap = WMEurope;
                    paintComponent(getGraphics());
                }
            } else if (!currMap.equals(worldMap)) {
                currMap = worldMap;
                paintComponent(getGraphics());
            }
        }

        statusBar.setText(String.format("(%.2f; %.2f)", point.getLongitude(), point.getLatitude()));
    }
}
