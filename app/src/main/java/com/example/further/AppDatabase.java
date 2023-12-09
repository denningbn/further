package com.example.further;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {Settings.class, Run.class}, version = 2)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SettingsDAO settingsDao();
    public abstract RunDAO runDao();
}

