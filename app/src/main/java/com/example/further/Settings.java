package com.example.further;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Settings")
public class Settings {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "coarseFineAccuracy")
    public boolean coarseFineAccuracy;

    @ColumnInfo(name = "slowFastInterval")
    public boolean slowFastInterval;

    @ColumnInfo(name = "encrypt")
    public boolean encrypt;


    public Settings(){
        coarseFineAccuracy = true;
        slowFastInterval = true;
        encrypt = true;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
