package com.dol;

public class Parent {
    private final Coordinates curr;
    private final Coordinates parent;

    public Parent(int x, int y, Coordinates parent) {
        this.curr = new Coordinates(x ,y);
        this.parent = parent;
    }
}
