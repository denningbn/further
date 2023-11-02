package com.example.further;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SettingsDAO {
    @Insert
    void insert(Settings settings);
    @Query("SELECT * FROM settings WHERE id IN (:userIds)")
    List<Settings> loadAllByIds(int[] userIds);

    @Delete
    void delete(Settings settings);
}
