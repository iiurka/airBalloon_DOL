package com.dol.ui.util;

public class GeoCoordinates {

    int x;
    int y;

    double longitude;
    double latitude;

    public GeoCoordinates(int x, int y, int width, int height, Region region) {

        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Illegal 2d vector: (" + x + "; " + y + ")");

        if (width < 0 || height < 0)
            throw new IllegalArgumentException("Illegal size of map");

        this.x = x;
        this.y = y;

        switch (region) {

            case WORLD -> {
                longitude = (x - (double) width / 2) / width * 360;
                latitude = -(y - (double) height / 2) / height * 180;
            }

            case EUROPE -> {
                longitude = -25 + (double) (x) / width * 75;
                latitude = 71 + (double) (-y) / height * 36;
            }
        }
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
