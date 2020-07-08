package com.dol;

import java.util.Objects;

public class Way implements Comparable<Way> {
    private final Coordinates c;
    private final double rough;
    private final long time;

    public Way(Coordinates c, double rough, long time) {
        this.c = c;
        this.rough = rough;
        this.time = time;
    }

    public int getLa() {
        return c.getLa();
    }

    public int getLo() {
        return c.getLa();
    }

    public double getRough() {
        return rough;
    }

    public Coordinates getCoordinates() {return this.c; }


    @Override
    public int compareTo(Way way) {
        if (rough < way.rough) {
            return -1;
        } else if (rough > way.rough) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Way way = (Way) o;
        return Double.compare(way.rough, rough) == 0 &&
                time == way.time &&
                Objects.equals(c, way.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, rough, time);
    }
}

