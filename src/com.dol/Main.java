package com.dol;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;


public class Main {
    public static void main(String[] args) {
        String Premium_API_Key = "ff481696fb3c4bf9bbe124403201007";
        String Premium_API_HTTP = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=ff481696fb3c4bf9bbe124403201007";



        Dijkstra dij = new Dijkstra();

        Coordinates from = new Coordinates(600, 300);
        Coordinates to = new Coordinates(605, 320);
        /* Крч, Юра, если ты адово горишь желаниием порисовать графики, то, как работает это дерьмо
           После того как заюзал dijkstra сюда передается стартовая точка
           Можно использовать getWayFromCoordinates, сюда передаешь конечную точку, получаешь список координат, если идти по которым получишь кратчайщий путь
           ЧТОБЫ ТЕБЕ НЕ КОМПИЛИТЬ КУЧУ РАЗ, Я ИХ ПОКА РАЗДЕЛИЛ, ТЕБЕ НЕ ОБЯЗАТЕЛЬНО ЮЗАТЬ ВСЕ ВРЕМЯ dijkstra МОЖЕШЬ ОДИН РАЗ ЗАДАТЬ НАЧАЛО, А ПОТОМ ПОЛУЧАТЬ КУЧУ ПУТЕЙ В РАЗНЫЕ ТОЧКЧИ
         */

        dij.dijkstra(from, to, 15, true);
        LinkedList<Coordinates> LL = dij.getWayFromCoordinates(to);

    }
}