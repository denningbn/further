package com.example.further;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SettingsDAO {
    @Insert
    long insert(Settings settings);
    @Query("SELECT * FROM settings WHERE id IN (:userIds)")
    List<Settings> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM settings where id = :userId")
    Settings getSettingsById(long userId);

    @Delete
    void delete(Settings settings);

    @Query("SELECT * FROM Settings WHERE id = 1")
    Settings getSettings();

    @Update
    void update(Settings settings);
}
