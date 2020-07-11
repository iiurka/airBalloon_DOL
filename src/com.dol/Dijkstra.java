package com.dol;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import org.apache.commons.io.*;
import org.json.JSONObject;

public class Dijkstra {
    Set<Coordinates> A; // мно-во не посещенных вершин
    Set<Coordinates> B; // мно-во посещенных вершин
    Map<Coordinates, Wind> windMap;
    int speed;

    Map<Coordinates, Double> minWay; // словарь кратчайщих путей
    Map<Coordinates, Coordinates> parentCord;

    LinkedList<Coordinates> list; // Тут хранится список координат, по которым будет лететь шар до цели

    PriorityQueue<Way> priorityQueue;

    public void dijkstra(Coordinates s, int speed){
        A = new HashSet<>();
        B = new HashSet<>();
        minWay = new HashMap<>();
        parentCord = new HashMap<>();
        priorityQueue = new PriorityQueue<>();
        this.speed = speed;

        for (int la = 350; la <= 710; la++) { // вот тут задается карта la - latitude(широта)
            for (int lo = -250; lo <= 500; lo++) { // lo - longitude(долгота)
                Coordinates a = new Coordinates(la, lo);
                A.add(a); // Множества не посещенных вершин
                minWay.put(a, Double.MAX_VALUE); // Минимальное расстояние до координат
                parentCord.put(a, null);
            }
        }

        addCoordinates(s, 0.0); // Начальная точка

        while (!A.isEmpty()) { // пока есть непосещенные вершины продолжаем крутиться
            Way a = priorityQueue.poll();
            if (B.contains(a.getCoordinates())) // Если вершина, которая вышла из очереди уже была рассмотренна в множество B, скипаем ее нахуй
                continue;
            // a.getCogetCoordinates() еще не рассмотренная координата, a.getRough() минимальное возможное расстояние до нее(с точки зрения алгоса)
            addCoordinates(a.getCoordinates(), a.getRough());
        }


        Coordinates to = new Coordinates(501, 107); // Здесь выбирать куда долетели
        System.out.print("Dijkstra: ");
        System.out.println(minWay.get(to));
        System.out.print("distanceFormula: "); // Это вот там формула, из учебника по географии
        System.out.println(distanceFormula(s, to));
    }

    private void addCoordinates(Coordinates s, double r) {
        int currLa = s.getLa();
        int currLo = s.getLo();

        minWay.replace(s, r); // Фиксируем найденное минимальное расстояние до точки
        A.remove(s); // Удаляем вершину из не рассмотренных
        B.add(s); // Помечаем как рассмотенную

        // Вот это штука, по сути пробегается по всем смежным клеткам
        /* x - текущая вершина, * - клетки в которые из нее можно попасть
         * * *
         * x *
         * * *
         */
        for(int la = -1; la <= 1; la++) {
            for(int lo = -1; lo <= 1; lo++) {
                Coordinates newS = adjCoordinates(currLa + la, currLo + lo); // Проверка на то, что смежная клетка не вышла за границы области

                if (newS == null || B.contains(newS)) { // если вершина не вышла за границы и уже не была рассмотрена(B.contains(newS)), то продолжаем с ней танцевать
                    continue;
                }
                double oldR = minWay.get(newS);
                double newR = r + distanceFormula(s, newS);
                if ((oldR > newR)) { // Если новый путь, короче уже известного, то добавляем координаты в очередь и меняем минимальный путь, в противном случае вершина опять идет нахуй
                    minWay.replace(newS, newR);
                    parentCord.replace(newS, s);
                    priorityQueue.add(new Way(newS, newR, 0));
                }
            }
        }
    }

    private Coordinates adjCoordinates(int la, int lo) {
        if (la >= 350 && la <= 710 && -250 <= lo && lo <= 500) {
            return new Coordinates(la, lo);
        }
        return null;
    }

    private int[] setTimeOfFlight(Coordinates c) {
        Coordinates coordinates = new Coordinates(c.getLa()/10, c.getLo()/10);
        Wind wind = null;

        int[] resultingSpeed = new int[8];

        if ((wind = windMap.get(coordinates)) == null) {
            String urlS = MessageFormat.format("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=e8f9a75259284658a76173713201007&q={0},{1}&mca=no&tp=1&num_of_days=1&format=json",
                    coordinates.getLa(), coordinates.getLo());
            try {
                JSONObject json = new JSONObject(IOUtils.toString(new URL(urlS), StandardCharsets.UTF_8));
                JSONObject weatherOnTime = json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0);
                wind = new Wind(weatherOnTime.getInt("windspeedKmph"), weatherOnTime.getInt("winddirDegree"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            windMap.put(coordinates, wind);
        }

//        for (int i = 0; i < 8; i++) {
//            int shift =
//            int resDegree =
//        }
        return resultingSpeed;
    }

    // НЕ СМОТРИ СЮДА, только если решил развлечься с точностью
    /*
    широта и долгота задается через целые числа, потому что для них куда проще считать хэши и нет коллизий с точностью
    у меня точность до 0.1, так что все координаты выгляд как-то так: 85.5 == 855
     */

    private double distanceFormula(Coordinates c1, Coordinates c2) {
        double fi1 = ((double)c1.getLa())/10.0;
        fi1 = Math.toRadians(fi1);

        double fi2 = ((double)c2.getLa())/10.0;
        fi2 = Math.toRadians(fi2);

        double ly1 = ((double)c1.getLo())/10.0;
        ly1 = Math.toRadians(ly1);

        double ly2 = ((double)c2.getLo()/10.0);
        ly2 = Math.toRadians(ly2);
        // если решишь, что тебе зачем-то нужно расстояние в м, то 6363.564 замени на 6363564.0
        return 2 * 6363.564 * Math.asin(Math.sqrt(haverSinus(fi2 - fi1) + Math.cos(fi1) * Math.cos(fi2) * haverSinus(ly2 - ly1)));
    }

    private double haverSinus(double x) {
        return Math.pow(Math.sin(x/2.0),2);
    }

    public LinkedList<Coordinates> getWayFromCoordinates(Coordinates b) {
        list = new LinkedList<>();

        Coordinates c = b;
        list.addFirst(c);
        while ((c = parentCord.get(c)) != null) {
            list.addFirst(c);
        }

        return list;
    }

    public class Wind {
        int speed;
        int degree;

        public Wind(int speed, int degree) {
            this.speed = speed;
            this.degree = degree;
        }
    }

}