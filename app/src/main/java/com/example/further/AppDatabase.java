package com.example.further;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Settings.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SettingsDAO settingsDao();
}

