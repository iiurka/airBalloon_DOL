package com.dol.ui.util;

public class GeoCoordinates {

    int x;
    int y;

    double longitude;
    double latitude;

    public GeoCoordinates(int x, int y, int width, int height) {

        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Illegal 2d vector: (" + x + "; " + y + ")");

        if (width < 0 || height < 0)
            throw new IllegalArgumentException("Illegal size of map");


        this.x = x;
        this.y = y;

        double longitude = (x - (double) width / 2) / width * 360;
        double latitude = -(y - (double) height / 2) / height * 180;

        if (longitude > 180.0) {
            longitude -= 360.0;
        }

        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
