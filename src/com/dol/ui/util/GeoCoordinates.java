package com.dol.ui.util;

public class GeoCoordinates {

    Integer x = null;
    int y;

    double longitude;
    double latitude;

    public GeoCoordinates() {}

    public GeoCoordinates(Integer x, int y, int width, int height) {

        if (x < 0 || y < 0 || width < 0 || height < 0)
            throw new IllegalArgumentException("Illegal 2d vector: (" + x + "; " + y + ")");

        this.x = x;
        this.y = y;

        //выравнивание по нулевой меридиане
        double longitude = (x - (double) width / 2 + 10) / (width - 15) * 360;
        double latitude = -(y - (double) (height - 54) / 2) / (height - 54) * 180;

        if (longitude > 180.0) {
            longitude -= 360.0;
        }

        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int[] getCartesianCoords() {
        return new int[] {x, y};
    }

    public double[] getGeographicCoords() {
        return new double[] {longitude, latitude};
    }

    public void clear() {
        x = null;
    }

    public boolean isEmpty() {
        return x == null;
    }
}
