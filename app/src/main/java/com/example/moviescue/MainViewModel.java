package com.example.moviescue;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moviescue.database.MovieDatabase;
import com.example.moviescue.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movies;

    public MainViewModel( @NonNull Application application ) {
        super(application);

        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        movies = database.movieDao().loadAllMovies();

    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
