package com.dol;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;

public class DijkstraTest {

    @Test
    public void getWayFromCoordinates() {
        Dijkstra dijkstraVert = new Dijkstra();
        Dijkstra dijkstraHor = new Dijkstra();
        dijkstraVert.parentCord = new HashMap<>();
        dijkstraHor.parentCord = new HashMap<>();
        LinkedList<Coordinates> listVert = new LinkedList<>();
        LinkedList<Coordinates> listHor = new LinkedList<>();
        Coordinates start = new Coordinates(455, 242);
        listVert.add(start);
        listHor.add(start);
        for (int i = 0; i < 10; i++) {
            Coordinates coordVert1 = new Coordinates(456+i, 242);
            Coordinates coordVert2 = new Coordinates(455+i, 242);
            Coordinates coordHor1 = new Coordinates(455, 243+i);
            Coordinates coordHor2 = new Coordinates(455, 242+i);
            dijkstraVert.parentCord.put(coordVert1, coordVert2);
            dijkstraHor.parentCord.put(coordHor1, coordHor2);
            listVert.add(coordVert1);
            listHor.add(coordHor1);
        }
        Assert.assertEquals(listVert, dijkstraVert.getWayFromCoordinates(new Coordinates(465,242)));
        Assert.assertEquals(listHor, dijkstraHor.getWayFromCoordinates(new Coordinates(455,252)));
    }


    @Test
    public void setTimeOfFlight() {
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.windMap = new HashMap<>();
        dijkstra.speed = 10;
        Coordinates coordinates = new Coordinates(420, 270);
        Wind wind = new Wind(15, 180);
        dijkstra.windMap.put(coordinates, wind);
        double[] expected = {0, -11, -7, 0, 0, 16, 26, 0};
        for (int i = 0; i < 8; i++) {
            Assert.assertEquals(expected[i], dijkstra.setTimeOfFlight(new Coordinates(425, 277))[i], 1e-6);
        }
    }
}