package com.dol;

import java.util.Objects;

public class Way implements Comparable<Way> {
    private final Coordinates c;
    private final double distance;
    private final double time;

    public Way(Coordinates c, double distance, double time) {
        this.c = c;
        this.distance = distance;
        this.time = time;
    }

    public double getDistance() { return distance; }

    public Coordinates getCoordinates() { return this.c; }

    public double getTime() { return this.time; }


    @Override
    public int compareTo(Way way) {
        if (time < way.time) {
            return -1;
        } else if (time > way.time) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Way way = (Way) o;
        return Double.compare(way.distance, distance) == 0 &&
                Double.compare(way.time, time) == 0 &&
                Objects.equals(c, way.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, distance, time);
    }
}