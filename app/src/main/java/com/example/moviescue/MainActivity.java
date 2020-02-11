package com.example.moviescue;

import com.example.moviescue.adapters.MovieAdapter;
import com.example.moviescue.database.MovieDatabase;
import com.example.moviescue.model.Movie;
import com.example.moviescue.utils.JsonUtils;
import com.example.moviescue.utils.MainActivityAsyncTask;
import com.example.moviescue.adapters.MovieAdapter.MovieAdapterOnClickHandler;
import com.example.moviescue.utils.NetworkUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.DisplayMetrics;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler,
        SwipeRefreshLayout.OnRefreshListener,
        MainActivityAsyncTask.OnTaskCompleted {

    private RecyclerView movieRecycler;
    private MovieAdapter adapter;
    private ProgressBar updateIndicator;
    private TextView errorMessage;
    private MenuItem popularityFilter;
    private MenuItem reviewFilter;
    private MenuItem favoriteFilter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private MovieDatabase movieDb;

    private final String POPULARITY = "popularity";
    private final String REVIEW = "review";
    private final String FAVORITE = "favorite";
    private String filter = POPULARITY;        // By default filter popularity is used
    private float POSTER_WIDTH = 185;
    private float POSTER_HEIGHT = 300;
    private float ASPECT_RATIO = POSTER_WIDTH / POSTER_HEIGHT;
    private ArrayList<Movie> moviesList;

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null || !savedInstanceState.containsKey("moviesList")) {

            // ....if no state was saved
            moviesList = new ArrayList<Movie>();

        } else {
            moviesList = savedInstanceState.getParcelableArrayList("moviesList");
            filter = savedInstanceState.getString("filterId");
        }

        setContentView(R.layout.activity_main);

        // ....finding views
        errorMessage = findViewById(R.id.error_message);
        movieRecycler = findViewById(R.id.movie_recycler);
        updateIndicator = findViewById(R.id.update_indicator);


        // ....setting up RecyclerView
        adapter = new MovieAdapter(this, this);
        movieRecycler.setAdapter(adapter);
        movieRecycler.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
        movieRecycler.setHasFixedSize(true);


        // ....setting up the swipe refresh action
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        // ....creating database
        movieDb = MovieDatabase.getInstance(getApplicationContext());
        setupViewModel();


        updateActivity(filter);


    }


    @Override
    public void onRefresh() {
        updateActivity(filter);

        //setRefreshing(false) will hide the indicator
        swipeRefreshLayout.setRefreshing(false);
    }


    /**
     * This method is overridden by MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movie The movie poster that was clicked
     */

    @Override
    public void onClick( Movie movie ) {
        Context context = this;
        Class movieDetail = MovieDetail.class;
        Intent newIntent = new Intent(context, movieDetail);
        newIntent.putExtra("movie", movie);
        startActivity(newIntent);
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.query_settings, menu);

        popularityFilter = menu.findItem(R.id.sort_1);
        reviewFilter = menu.findItem(R.id.sort_2);
        favoriteFilter = menu.findItem(R.id.sort_3);
        setFilter(filter);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        int id = item.getItemId();

        if (id == R.id.sort_1) {

            setFilter(POPULARITY);
            updateActivity(filter);
            return true;

        } else if (id == R.id.sort_2) {

            setFilter(REVIEW);
            updateActivity(filter);
            return true;

        } else if (id == R.id.sort_3) {

            setFilter(FAVORITE);
            updateActivity(filter);
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }


    }


    private void setFilter( String filterId ) {


        switch (filterId) {

            case POPULARITY:
                popularityFilter.setChecked(true);
                reviewFilter.setChecked(false);
                favoriteFilter.setChecked(false);
                filter = POPULARITY;
                break;


            case REVIEW:
                popularityFilter.setChecked(false);
                reviewFilter.setChecked(true);
                favoriteFilter.setChecked(false);
                filter = REVIEW;
                break;


            case FAVORITE:
                popularityFilter.setChecked(false);
                reviewFilter.setChecked(false);
                favoriteFilter.setChecked(true);
                filter = FAVORITE;
                break;


            default:
                break;
        }

    }


    private void showMoviesView() {

        errorMessage.setVisibility(View.INVISIBLE);
        movieRecycler.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {

        movieRecycler.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }


    /**
     * This method will get the user's sorting preference and
     * invoke other method to query the TMDB api
     */

    private void updateActivity( String filter ) {

        if (filter.equals(FAVORITE)) {
            setupViewModel();
        } else {
            errorMessage.setVisibility(View.INVISIBLE);
            movieRecycler.setVisibility(View.VISIBLE);


            MainActivityAsyncTask fetchMovieData = new MainActivityAsyncTask(MainActivity.this);
            fetchMovieData.execute(urlBuilder(filter));
        }


    }


    @Override
    public void onSaveInstanceState( @NonNull Bundle outState ) {
        outState.putParcelableArrayList("moviesList", moviesList);
        outState.putString("filterId", filter);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void preExecute() {

        updateIndicator.setVisibility(View.VISIBLE);
    }


    @Override
    public void onTaskCompleted( String queryResponse ) {

        updateIndicator.setVisibility(View.INVISIBLE);
        if (queryResponse != null) {
            showMoviesView();
            moviesList = JsonUtils.parseMovieListJson(queryResponse);
            adapter.setMoviesList(moviesList);
        } else {
            showErrorMessage();
        }

    }


    private int numberOfColumns() {

        // get Display dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        //get Display orientation
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            float columns = (width * 2) / (height * ASPECT_RATIO);
            return Math.round(columns);
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            float columns = (width * 2) / (height * ASPECT_RATIO);
            return Math.round(columns);
        }

        return 1;
    }


    private URL urlBuilder( String filter ) {

        URL apiQuery = NetworkUtils.buildUrl(filter);

        return apiQuery;
    }


    private void setupViewModel() {

        MainViewModel viewModel = new MainViewModel(getApplication());
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged( @Nullable List<Movie> movieEntries ) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");

                if (filter.equals(FAVORITE)) {
                    moviesList = (ArrayList<Movie>) movieEntries;
                    showMoviesView();
                    adapter.setMoviesList(moviesList);
                }

            }
        });
    }


}
