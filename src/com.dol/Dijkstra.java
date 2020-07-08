package com.dol;

import java.util.*;

public class Dijkstra {
    Set<Coordinates> A; // мно-во не посещенных вершин
    Set<Coordinates> B; // мно-во посещенных вершин

    Map<Coordinates, Double> minWay; // словарь кротчайщих путей
    PriorityQueue<Way> priorityQueue;

    public void dijkstra(Coordinates s){
        A = new HashSet<>();
        B = new HashSet<>();
        minWay = new HashMap<>();
        priorityQueue = new PriorityQueue<>();

        for(int la = -860; la < 840; la+=10) { // la - latitude(широта)
            for (int lo = -1800; lo <= 1800; lo+=10) { // lo - longitude(долгота)
                B.add(new Coordinates(la, lo));
                minWay.put(new Coordinates(la, lo), Double.MAX_VALUE);
            }
        }

        B.remove(s);
        A.add(s);
        addCoordinates(s, 0.0);

        while (!B.isEmpty()) {
            Way minWay = priorityQueue.poll();

            try {
                if (A.contains(minWay.getCoordinates())) {
                    continue;
                }
            } catch (NullPointerException e) {
                System.out.println(A.size());
                System.out.println(minWay.getCoordinates().getLa() + " " + minWay.getCoordinates().getLo());
                throw e;
            }

            B.remove(minWay.getCoordinates());
            A.add(minWay.getCoordinates());

            addCoordinates(minWay.getCoordinates(), minWay.getRough());
        }
    }

    private void addCoordinates(Coordinates c, double r) {
        int currLa = c.getLa();
        int currLo = c.getLo();

        if (currLa == 850 || currLa == -850) {
            return;
        }
        minWay.replace(c, r);
        double rough = minWay.get(c);
        for(int la = -1; la <= 1; la ++) { // la - latitude(широта)
            for (int lo = -1; lo <= 1; lo ++) { // lo - longitude(долгота)
                if (!(la == 0 && lo == 0)) {
                    Coordinates a = new Coordinates(currLa + la, currLo + lo);
                    a = allCheck(a);
                    if (a.getLa() == 850 || a.getLa() == -850) {
                        return;
                    }
                    if (minWay.get(a) > rough + distanceFormula(c, a)) {
                        priorityQueue.add(new Way(a, rough + distanceFormula(c, a), 0));
                    }
                }
            }
        }
    }

    private double distanceFormula(Coordinates c1, Coordinates c2) {
        double fi1 = ((double)c1.getLa())/10.0;
        fi1 = Math.toRadians(fi1);

        double fi2 = ((double)c2.getLa())/10.0;
        fi2 = Math.toRadians(fi2);

        double ly1 = ((double)c1.getLo())/10.0;
        ly1 = Math.toRadians(ly1);

        double ly2 = ((double)c2.getLo()/10.0);
        ly2 = Math.toRadians(ly2);

        return 2 * 6363 * Math.asin(Math.sqrt(haversinus(fi2 - fi1) + Math.cos(fi1) * Math.cos(fi2) * haversinus(ly2 - ly1)));
    }

    private double haversinus(double x) {
        return Math.pow(Math.sin(x/2.0),2);
    }

    private Coordinates allCheck(Coordinates c) {
        c = checkLoPlus(c);
        c = checkLoMinus(c);
        return c;
    }

    private Coordinates checkLoPlus(Coordinates c) {
        if (c.getLo() == 1801) {
            return new Coordinates(c.getLa(), -1799);
        }
        return c;
    }

    private Coordinates checkLoMinus(Coordinates c) {
        if (c.getLo() == -1801) {
            return new Coordinates(c.getLa(), 1799);
        }
        return c;
    }
}


