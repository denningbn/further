package com.example.further;

public class Run {

    public long id;
    private double distance;

    private double bestOneMile;
    private double bestOneKilometer;

    public Run(long _id, double _distance){
        id = _id;
        distance = _id;
        bestOneMile = 0;
        bestOneKilometer = 0;
    }

    public double getBestOneKilometer() {
        return bestOneKilometer;
    }

    public double getBestOneMile() {
        return bestOneMile;
    }

    public double getDistance() {
        return distance;
    }

    public String toString(){
        return ("ID: " + id + "\nDistance: " + distance);
    }

}
