package com.example.further;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

public interface RunDAO {
    @Insert
    long insert(Run run);

    @Delete
    void delete(Run run);

    @Query("SELECT * FROM RunTable WHERE id = :runId")
    Settings getRunById(long runId);

    @Update
    void update(Run run);
}