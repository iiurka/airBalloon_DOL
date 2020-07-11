package com.dol;

import java.util.Objects;

public class Coordinates {
    private final int lat;
    private final int lon;

    public Coordinates(int lat, int lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public int getLat() { return lat; }
    public int getLon() { return lon; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return lat == that.lat &&
                lon == that.lon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}
