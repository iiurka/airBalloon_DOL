package com.dol;


import java.lang.reflect.Field;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {

        Dijkstra dij = new Dijkstra();

        Coordinates from = new Coordinates(600, 400);
        Coordinates to = new Coordinates(605, 405);
        /* Крч, Юра, если ты адово горишь желаниием порисовать графики, то, как работает это дерьмо
           После того как заюзал dijkstra сюда передается стартовая точка
           Можно использовать getWayFromCoordinates, сюда передаешь конечную точку, получаешь список координат, если идти по которым получишь кратчайщий путь
           ЧТОБЫ ТЕБЕ НЕ КОМПИЛИТЬ КУЧУ РАЗ, Я ИХ ПОКА РАЗДЕЛИЛ, ТЕБЕ НЕ ОБЯЗАТЕЛЬНО ЮЗАТЬ ВСЕ ВРЕМЯ dijkstra МОЖЕШЬ ОДИН РАЗ ЗАДАТЬ НАЧАЛО, А ПОТОМ ПОЛУЧАТЬ КУЧУ ПУТЕЙ В РАЗНЫЕ ТОЧКЧИ
         */

        dij.dijkstra(from, 15);
        LinkedList<Coordinates> LL = dij.getWayFromCoordinates(to);

    }
}