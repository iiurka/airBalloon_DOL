package com.dol;


public class Main {
    public static void main(String[] args) {
        Dijkstra dij = new Dijkstra();
        dij.dijkstra(new Coordinates(550, 850));
    }
}