package com.example.further;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RunTable")
public class Run {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "distance")
    private double distance;

    @ColumnInfo(name = "bestOneMile")
    private double bestOneMile;
    @ColumnInfo(name = "bestOneKilometer")
    private double bestOneKilometer;

    public Run(double _distance){
        distance = _distance;
        bestOneMile = 0;
        bestOneKilometer = 0;
    }

    public Run()
    {

    }

    public long getId(){
        return id;
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


    public void setDistance(double _distance){
        this.distance = _distance;
    }

    public void setBestOneMile(double _bestOneMile){
        bestOneMile = _bestOneMile;
    }


    public void setBestOneKilometer(double _bestOneKilometer){
        bestOneMile = _bestOneKilometer;
    }
    public String toString(){
        return ("ID: " + id + "\nDistance: " + distance);
    }
}
