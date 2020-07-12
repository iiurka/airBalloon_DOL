package com.dol.ui.elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

import com.dol.Coordinates;
import com.dol.Dijkstra;
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

    private final Image europeMap;
    private final Image balticSeaMap;
    private final Image blackSeaMap;
    private final Image EMBaltic;
    private final Image EMBlack;

    private Image currMap;

    private Region region = Region.EUROPE;

    private int widthMap;
    private int heightMap;

    Dijkstra algorithm;
    Point A;
    Point B;

    public ImagePanel(JFrame frame, ToolBar toolBar, JLabel statusBar) throws FileNotFoundException {
        algorithm = new Dijkstra();
        this.frame = frame;

        toolBar.observer = new ImagePanelObserver(this);
        this.toolBar = toolBar;
        this.statusBar = statusBar;

        addMouseListener(this);
        addMouseMotionListener(this);

        try {
            europeMap = ImageIO.read(new File("resources" + File.separator + "europe map.jpg"));
            currMap = europeMap;
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
            balticSeaMap = ImageIO.read(new File("resources" + File.separator + "baltic sea.jpg"));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"baltic sea.jpg\" not found");
        }

        try {
            blackSeaMap = ImageIO.read(new File("resources" + File.separator + "black sea.jpg"));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"black sea.jpg\" not found");
        }

        try {
            EMBaltic = ImageIO.read(new File("resources" + File.separator + "EM with baltic.jpg"));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"EM with baltic.jpg\" not found");
        }

        try {
            EMBlack = ImageIO.read(new File("resources" + File.separator + "EM with black.jpg"));
        } catch (IOException e) {
            throw new FileNotFoundException("File \"EM with black.jpg\" not found");
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
        getGraphics().drawImage(point.getImagePoint(), point.getX() - 25, point.getY() - 50, 50, 50, null);
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

    public Region getRegion() {
        return region;
    }

    public void openSpeedControllerWindow() {
        new SpeedControllerWindow(frame).setVisible(true);
    }
    public void setDefaultRegion() {
        A.deletePoint();
        B.deletePoint();
        region = Region.EUROPE;
        currMap = europeMap;
        paintComponent(getGraphics());
    }

    public void run() {

        if (currMap.equals(europeMap)) {
            JOptionPane.showMessageDialog(frame, "Please select a region!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        clearMap();
        paintPoint(A);
        paintPoint(B);
        if (!A.isWorth()) {
            JOptionPane.showMessageDialog(frame, "Please select a departure point!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!B.isWorth()) {
            JOptionPane.showMessageDialog(frame, "Please select an arrival point!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Coordinates from = new Coordinates((int) (A.getLatitude() * 10), (int) (A.getLongitude() * 10));
        Coordinates to = new Coordinates((int) (B.getLatitude() * 10), (int) (B.getLongitude() * 10));


        switch (region) {
            case BALTIC_SEA :
                algorithm.dijkstra(from, to, SpeedControllerWindow.getCurrentSpeed(), true);
                break;

            case BLACK_SEA :
                algorithm.dijkstra(from, to, SpeedControllerWindow.getCurrentSpeed(), false);
                break;
        }

        LinkedList <Coordinates> temp = algorithm.getWayFromCoordinates(to);

        if (temp == null) {
            JOptionPane.showMessageDialog(frame, "Невозможно добраться до пункта назначения!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(frame, String.format("Distance = %.2f km\nTime = %dh:%dm", algorithm.getDistance(to), (int) algorithm.getTime(to), (int) ((algorithm.getTime(to) - (int) algorithm.getTime(to)) * 60)), "Complete!", JOptionPane.INFORMATION_MESSAGE);
        Coordinates[] result = new Coordinates[temp.size()];
        temp.toArray(result);

        for (int i = 1; i < result.length; i++) {

            Coordinates prev = result[i-1];
            Coordinates curr = result[i];

            int prevX = (int) ((prev.getLon() / 10.0 + 25) * widthMap / 75);
            int prevY = (int) ((71 - prev.getLat() / 10.0) * heightMap / 36);

            int currX = (int) ((curr.getLon() / 10.0 + 25) * widthMap / 75);
            int currY = (int) ((71 - curr.getLat() / 10.0) * heightMap / 36);

            switch (region) {

                case BALTIC_SEA : {
                    prevX = (int) ((prev.getLon() / 10.0 - 5.30) * widthMap / 27.20);
                    prevY = (int) ((66.5 - prev.getLat() / 10.0) * heightMap / 13.5);
                    currX = (int) ((curr.getLon() / 10.0 - 5.30) * widthMap / 27.20);
                    currY = (int) ((66.5 - curr.getLat() / 10.0) * heightMap / 13.5);
                    break;
                }

                case BLACK_SEA : {
                    prevX = (int) ((prev.getLon() / 10.0 - 25.3) * widthMap / 17.9) + 6;
                    prevY = (int) ((47.7 - prev.getLat() / 10.0) * heightMap / 8.1) - 6;
                    currX = (int) ((curr.getLon() / 10.0 - 25.3) * widthMap / 17.9) + 6;
                    currY = (int) ((47.7 - curr.getLat() / 10.0) * heightMap / 8.1) - 6;
                    break;
                }
            }

            Graphics2D g = (Graphics2D) getGraphics();
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(2));
            g.drawLine(prevX, prevY, currX, currY);
        }

        paintPoint(A);
        paintPoint(B);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        GeoCoordinates point = new GeoCoordinates(e.getX(), e.getY(), widthMap, heightMap, region);

        if (region == Region.EUROPE) {


            if (5.3 < point.getLongitude() && point.getLongitude() < 32.5 &&
                    53 < point.getLatitude() && point.getLatitude() < 66.5 ) {

                region = Region.BALTIC_SEA;
                currMap = balticSeaMap;

                toolBar.resetPressedAction();
                paintComponent(getGraphics());

            } else if (25.3 < point.getLongitude() && point.getLongitude() < 43.2 &&
                    39.6 < point.getLatitude() && point.getLatitude() < 47.7 ) {

                region = Region.BLACK_SEA;
                currMap = blackSeaMap;

                toolBar.resetPressedAction();
                paintComponent(getGraphics());

            } else {
                if (toolBar.getPressedAction() == ToolBar.Action.SET_A || toolBar.getPressedAction() == ToolBar.Action.SET_B) {
                    JOptionPane.showMessageDialog(frame, "Please select a region!", "Warning!", JOptionPane.WARNING_MESSAGE);
                    toolBar.resetPressedAction();
                }
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
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {

        GeoCoordinates point = new GeoCoordinates(e.getX(), e.getY(), widthMap, heightMap, region);

        if (region == Region.EUROPE) {
            if (5.3 < point.getLongitude() && point.getLongitude() < 32.5 &&
                  53 < point.getLatitude() && point.getLatitude() < 66.5 ) {

                if (!currMap.equals(EMBaltic)) {
                    currMap = EMBaltic;
                    paintComponent(getGraphics());
                }

            } else if (25.3 < point.getLongitude() && point.getLongitude() < 43.2 &&
                        39.6 < point.getLatitude() && point.getLatitude() < 47.7 ) {

                if (!currMap.equals(EMBlack)) {
                    currMap = EMBlack;
                    paintComponent(getGraphics());
                }

            } else if (!currMap.equals(europeMap)) {
                currMap = europeMap;
                paintComponent(getGraphics());
            }
        }
        statusBar.setText(String.format("(%.2f; %.2f)", point.getLongitude(), point.getLatitude()));
    }
}
