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

    @ColumnInfo(name = "distance")
    private double bestOneMile;
    @ColumnInfo(name = "distance")
    private double bestOneKilometer;

    public Run(double _distance){
        distance = _distance;
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
