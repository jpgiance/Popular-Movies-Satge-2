package com.example.moviescue.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moviescue.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {


    @Query("SELECT * FROM movie ORDER BY title")
    LiveData<List<Movie>> loadAllTasks();

    @Insert
    void insertTask(Movie taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Movie taskEntry);

    @Delete
    void deleteTask(Movie taskEntry);

    @Query("SELECT * FROM movie WHERE tableId = :tableId")
    LiveData<Movie> loadTaskByTableId( int tableId);


}
