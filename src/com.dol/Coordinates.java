package com.dol;

import java.util.Objects;

public class Coordinates {
    private final int la;
    private final int lo;

    public Coordinates(int la, int lo) {
        this.la = la;
        this.lo = lo;
    }

    public int getLa() { return la; }
    public int getLo() { return lo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return la == that.la &&
                lo == that.lo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(la, lo);
    }
}
