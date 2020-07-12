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
    Map<Coordinates, Double> minTime; // словарь минимальных времен
    Map<Coordinates, Coordinates> parentCord;

    LinkedList<Coordinates> list; // Тут хранится список координат, по которым будет лететь шар до цели

    PriorityQueue<Way> priorityQueue;

    boolean region;

    public Dijkstra() {
        windMap = new HashMap<>();
    }

    private void initRegion() {
        if (region) { // Baltic sea
            for (int lat = 530; lat <= 665; lat++) { // вот тут задается карта la - latitude(широта)
                for (int lon = 53; lon <= 325; lon++) { // lo - longitude(долгота)
                    Coordinates a = new Coordinates(lat, lon);
                    A.add(a); // Множества не посещенных вершин
                    minWay.put(a, Double.MAX_VALUE); // Минимальное расстояние до координат
                    minTime.put(a, Double.MAX_VALUE);
                    parentCord.put(a, null);
                }
            }
        } else { // Black sea
            for (int lat = 396; lat <= 477; lat++) { // вот тут задается карта la - latitude(широта)
                for (int lon = 253; lon <= 432; lon++) { // lo - longitude(долгота)
                    Coordinates a = new Coordinates(lat, lon);
                    A.add(a); // Множества не посещенных вершин
                    minWay.put(a, Double.MAX_VALUE); // Минимальное расстояние до координат
                    minTime.put(a, Double.MAX_VALUE);
                    parentCord.put(a, null);
                }
            }
        }
    }

    public void dijkstra(Coordinates from, Coordinates to, int speed, boolean region) {
        A = new HashSet<>();
        B = new HashSet<>();
        minWay = new HashMap<>();
        minTime = new HashMap<>();
        parentCord = new HashMap<>();
        priorityQueue = new PriorityQueue<>();
        this.speed = speed;
        this.region = region;

        initRegion();
        addCoordinates(from, 0.0, 0); // Начальная точка

        while (!priorityQueue.isEmpty()) { // пока есть непосещенные вершины продолжаем крутиться
            Way a = priorityQueue.poll();
            if (B.contains(a.getCoordinates())) // Если вершина, которая вышла из очереди уже была рассмотренна в множество B, скипаем ее нахуй
                continue;
            addCoordinates(a.getCoordinates(), a.getDistance(), a.getTime());
        }
    }

    private void addCoordinates(Coordinates s, double distance, double time) {
        int currLat = s.getLat();
        int currLon = s.getLon();

        int direction = -1;
        double [] wind = setTimeOfFlight(s);

        minWay.replace(s, distance); // Фиксируем найденное минимальное расстояние до точки
        minTime.replace(s, time);
        A.remove(s); // Удаляем вершину из не рассмотренных
        B.add(s); // Помечаем как рассмотенную

        for(int lat = -1; lat <= 1; lat++) {
            for(int lon = -1; lon <= 1; lon++) {
                if (!(lat == 0 && lon == 0)) {
                    direction++;
                }
                Coordinates newS = adjCoordinates(currLat + lat, currLon + lon); // Проверка на то, что смежная клетка не вышла за границы области

                if (newS == null || B.contains(newS)) { // если вершина не вышла за границы и уже не была рассмотрена(B.contains(newS)), то продолжаем с ней танцевать
                    continue;
                }

                if (wind[direction] <= 0) {
                    continue;
                }

                double dist = distanceFormula(s, newS);
                double oldTime = minTime.get(newS);
                double newTime = time + dist/wind[direction];
               // double newTime = time + 5;
                if ((oldTime > newTime)) {
                    double newDistance = distance + dist;

                    minWay.replace(newS, newDistance);
                    minTime.replace(newS, newTime);
                    parentCord.replace(newS, s);

                    priorityQueue.add(new Way(newS, newDistance, newTime));
                }
            }
        }
    }

    private Coordinates adjCoordinates(int lat, int lon) {
        if (region) {
            if (lat >= 530 && lat <= 665 && 53 <= lon && lon <= 325) {
                return new Coordinates(lat, lon);
            }
        } else {
            if (lat >= 396 && lat <= 477 && 253 <= lon && lon <= 432) {
                return new Coordinates(lat, lon);
            }
        }
        return null;
    }

    protected double[] setTimeOfFlight(Coordinates c) {
        Coordinates coordinates = new Coordinates(c.getLat()/10, c.getLon()/10);
        Wind wind;

        double[] resultingSpeed = new double[8];

        if ((wind = windMap.get(coordinates)) == null) {
            String urlS = MessageFormat.format("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=d3f5faa1c70b493781e161214201207&q={0},{1}&mca=no&tp=1&num_of_days=1&format=json",
                    coordinates.getLat(), coordinates.getLon());
            try {
                JSONObject json = new JSONObject(IOUtils.toString(new URL(urlS), StandardCharsets.UTF_8));
                JSONObject weatherOnTime = json.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0);
                int windDegree = (weatherOnTime.getInt("winddirDegree") + 180) % 360;
                int windSpeed = weatherOnTime.getInt("windspeedKmph");
                windDegree = 90 - windDegree % 90 - 90 * (windDegree / 90);
                wind = new Wind(windSpeed, windDegree);
            } catch (IOException e) {
                e.printStackTrace();
            }
            windMap.put(coordinates, wind);
        }

        wind.degree = 90 - wind.degree % 90 - 90 * (wind.degree / 90);
        for (int i = 0; i < 8; i++) {
            double windProjection = wind.speed*Math.cos(Math.toRadians(wind.degree + 45 * i));
            double engineProjection = Math.sqrt(Math.pow(speed, 2) - Math.pow(windProjection, 2));
            resultingSpeed[i] = (int)(engineProjection+wind.speed*Math.sin(Math.toRadians(wind.degree + 45 * i)));
//            System.out.println(resultingSpeed[i]);
        }
        return resultingSpeed;
    }

    // НЕ СМОТРИ СЮДА, только если решил развлечься с точностью
    /*
    широта и долгота задается через целые числа, потому что для них куда проще считать хэши и нет коллизий с точностью
    у меня точность до 0.1, так что все координаты выгляд как-то так: 85.5 == 855
     */

    private double distanceFormula(Coordinates c1, Coordinates c2) {
        double fi1 = ((double)c1.getLat())/10.0;
        fi1 = Math.toRadians(fi1);

        double fi2 = ((double)c2.getLat())/10.0;
        fi2 = Math.toRadians(fi2);

        double ly1 = ((double)c1.getLon())/10.0;
        ly1 = Math.toRadians(ly1);

        double ly2 = ((double)c2.getLon()/10.0);
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

        if (list.size() == 1)
            return null;

        return list;
    }

    public double getTime(Coordinates b) {
        return minTime.get(b);
    }

    public double getDistance(Coordinates b) {
        return minWay.get(b);
    }
}