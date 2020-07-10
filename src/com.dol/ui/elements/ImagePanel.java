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

    public void setWorldRegion() {
        A.deletePoint();
        B.deletePoint();
        region = Region.WORLD;
        currMap = worldMap;
        paintComponent(getGraphics());
    }

    public void run() {

        if (currMap.equals(worldMap)) {
            JOptionPane.showMessageDialog(frame, "Please select a region!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }

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


        Dijkstra algorithm = new Dijkstra();
        algorithm.dijkstra(from);
        LinkedList <Coordinates> temp = algorithm.getWayFromCoordinates(to);
        Coordinates[] result = new Coordinates[temp.size()];
        temp.toArray(result);

        for (int i = 2; i < result.length; i++) {

            Coordinates prev = result[i-1];
            Coordinates curr = result[i];

            int prevX = (int) ((prev.getLo() / 10.0 + 25) * widthMap / 75);
            int prevY = (int) ((71 - prev.getLa() / 10.0) * heightMap / 36);

            int currX = (int) ((curr.getLo() / 10.0 + 25) * widthMap / 75);
            int currY = (int) ((71 - curr.getLa() / 10.0) * heightMap / 36);

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

        if (region == Region.WORLD) {
            if (-25 < point.getLongitude() && point.getLongitude() < 50 &&
                    35 < point.getLatitude() && point.getLatitude() < 71) {
                region = Region.EUROPE;
                currMap = europeMap;

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
