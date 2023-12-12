package com.example.further;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SettingsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Settings settings);

    @Delete
    void delete(Settings settings);

    @Query("SELECT * FROM Settings WHERE id = 1")
    Settings getSettings();


    @Query("SELECT * FROM Settings WHERE id = :userId")
    Settings getSettingsById(long userId);

    @Update
    void update(Settings settings);
}
