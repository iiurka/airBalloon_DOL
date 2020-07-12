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

            case EUROPE : {
                longitude = -25 + (double) (x) / width * 75;
                latitude = 71 + (double) (-y) / height * 36;
                break;
            }

            case BALTIC_SEA : {
                longitude = 5.3 + (double) (x) / width * 27.2;
                latitude = 66.5 + (double) (-y) / height * 13.5;
                break;
            }

            case BLACK_SEA : {
                longitude = 25.3 + (double) (x) / width * 17.9;
                latitude = 47.7 + (double) (-y) / height * 8.1;
                break;
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


/*

for (int i = 0; i < 8; i++) {
    resultingSpeed[i] = wind.speed * Math.cos(Math.toRadians(wind.degree - 45 * i)) + speed;
}

 */