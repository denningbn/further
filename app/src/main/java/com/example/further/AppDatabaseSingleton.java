package com.example.further;

import android.content.Context;

import androidx.room.Room;

public class AppDatabaseSingleton {
    private static AppDatabase databaseInstance;

    public static AppDatabase getDatabaseInstance(Context context) {
        if (databaseInstance == null) {
            synchronized (AppDatabaseSingleton.class) {
                if (databaseInstance == null) {
                    databaseInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-database")
                            .build();
                }
            }
        }
        return databaseInstance;
    }
}