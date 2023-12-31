package com.example.further;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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


    @ColumnInfo(name = "pace")
    private String pace;

    @ColumnInfo(name ="lats")
    private ArrayList<Double> lats;

    @ColumnInfo(name ="longs")
    private ArrayList<Double> longs;


    @ColumnInfo(name = "insertionDate")
    private long insertionDate;

    public Run(double _distance, String _pace){
        distance = _distance;
        bestOneMile = 0;
        bestOneKilometer = 0;
        pace = _pace;
        insertionDate = new Date().getTime();

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


    public String dateToString(){
        Date date = new Date(insertionDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String foo = month + "/" + day + "/" + year + "---" + hour + ':' + minute;
        return foo;
    }

    public String getPace(){
        return this.pace;
    }

    public void setPace(String _pace){
        this.pace = _pace;
    }

    public long getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(long _insertionDate) {
        this.insertionDate = _insertionDate;
    }
    public String toString(){
        return (distance + " Miles\nTime: " + pace);
    }

    public ArrayList<Double> getLats()
    {
        return lats;
    }

    public ArrayList<Double> getLongs()
    {
        return longs;
    }

    public void setLats(ArrayList<Double> _lats)
    {
        this.lats = _lats;
    }

    public void setLongs(ArrayList<Double> _longs)
    {
        this.longs = _longs;
    }
}

