package com.dol.ui.util;

import java.awt.*;

public class Point {

    public GeoCoordinates coordinates;
    private final Image imagePoint;

    public Point(Image image) {
        imagePoint = image;
        coordinates = null;
    }

    public Image getImagePoint() {
        return imagePoint;
    }

    public boolean isWorth() {
        return coordinates != null;
    }

    public void deletePoint() {
        coordinates = null;
    }

    public void move(GeoCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void move(int x, int y, int width, int height, Region region) {
        coordinates = new GeoCoordinates(x, y, width, height, region);
    }

    public int getX() {
        return coordinates.getX();
    }

    public int getY() {
        return coordinates.getY();
    }
    public double getLongitude() {
        return coordinates.getLongitude();
    }
    public double getLatitude() {
        return coordinates.getLatitude();
    }
}