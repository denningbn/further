package com.example.further;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Settings")
public class Settings {
    @PrimaryKey(autoGenerate = false)
    public long id;

    @ColumnInfo(name = "coarseFineAccuracy")
    public boolean coarseFineAccuracy;

    @ColumnInfo(name = "slowFastInterval")
    public boolean slowFastInterval;

    @ColumnInfo(name = "encrypt")
    public boolean encrypt;

    @ColumnInfo(name = "dgMode")
    public boolean dgMode;


    public Settings(){
        id = 1;
        coarseFineAccuracy = true;
        slowFastInterval = true;
        encrypt = true;
        dgMode = false;
    }


    public Settings(long _id){
        id = _id;
        coarseFineAccuracy = true;
        slowFastInterval = true;
        encrypt = true;
        dgMode = false;
    }


    public long getId() {
        return id;
    }


    public void setCoarseFineAccuracy(boolean coarseFineAccuracy){
        this.coarseFineAccuracy = coarseFineAccuracy;
    }


    public void setSlowFastInterval(boolean slowFastInterval){
        this.slowFastInterval= slowFastInterval;
    }
    public void setEncrypt(boolean encrypt){
        this.encrypt=encrypt;
    }

    public void setDgMode(boolean _dgMode)
    {
        this.dgMode = _dgMode;
    }
    public boolean getDgMode()
    {
        return dgMode;
    }

    public String toString(){
       return ("ID: " + id + "\nEncrypt: " + encrypt + "\nAccuracy: " + coarseFineAccuracy + "\nInterval: " + slowFastInterval);
    }
}
