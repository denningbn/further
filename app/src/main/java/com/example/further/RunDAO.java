package com.example.further;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RunDAO {
    @Insert
    long insert(Run run);

    @Delete
    void delete(Run run);

    @Query("SELECT * FROM RunTable WHERE id = :runId")
    Run getRunById(long runId);

    @Update
    void update(Run run);

    @Query("SELECT * FROM RunTable")
    List<Run> getAllItems();

    @Query("SELECT * FROM RunTable ORDER BY insertionDate DESC")
    List<Run> getSortedRuns();
}