package com.example.further;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDate;

@Entity(tableName = "RunTable")
public class Run {
    @PrimaryKey(autoGenerate = false)
    public long id;
    @ColumnInfo(name = "distance")
    private double distance;

    @ColumnInfo(name = "bestOneMile")
    private double bestOneMile;
    @ColumnInfo(name = "bestOneKilometer")
    private double bestOneKilometer;


    @ColumnInfo(name = "dateMonth")
    private int dateMonth;

    @ColumnInfo(name = "dateYear")
    private int dateYear;


    @ColumnInfo(name = "dateDay")
    private int dateDay;

    public Run(double _distance){
        distance = _distance;
        id = 4;
        bestOneMile = 0;
        bestOneKilometer = 0;
        dateMonth = LocalDate.now().getMonthValue();
        dateDay = LocalDate.now().getDayOfMonth();
        dateYear = LocalDate.now().getYear();
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


    public int getDateMonth() {
        return dateMonth;
    }


    public int getDateDay() {
        return dateDay;
    }
    public int getDateYear() {
        return dateYear;
    }

    public void setDateMonth(int _dateMonth){
        dateMonth = _dateMonth;
    }

    public void setDateDay(int _dateDay){
        dateDay = _dateDay;
    }

    public void setDateYear(int _dateYear){
        dateYear = _dateYear;
    }


    public String toString(){
        return ("ID: " + id + "\nDistance: " + distance);
    }
}
