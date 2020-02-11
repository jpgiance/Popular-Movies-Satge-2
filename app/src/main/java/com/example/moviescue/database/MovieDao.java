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
    LiveData<List<Movie>> loadAllMovies();

    @Insert
    void insertTask( Movie movieEntry );

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask( Movie movieEntry );

    @Delete
    void deleteTask( Movie movieEntry );

    @Query("SELECT * FROM movie WHERE id = :id")
    Movie retrieveMovieId( int id );


}
