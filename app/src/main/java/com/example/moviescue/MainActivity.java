package com.example.moviescue;

import com.example.moviescue.adapters.MovieAdapter;
import com.example.moviescue.model.Movie;
import com.example.moviescue.utils.JsonUtils;
import com.example.moviescue.utils.MyAsyncTask;
import com.example.moviescue.adapters.MovieAdapter.MovieAdapterOnClickHandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler,
                                                                SwipeRefreshLayout.OnRefreshListener,
                                                                MyAsyncTask.OnTaskCompleted {

    private RecyclerView movieRecycler;
    private MovieAdapter adapter;
    private ProgressBar updateIndicator;
    private TextView errorMessage;
    private MenuItem popularityFilter;
    private MenuItem reviewFilter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private final String POPULARITY = "popularity";
    private final String REVIEW = "review";
    private String filter = POPULARITY;        // By default filter popularity is used
    private float POSTER_WIDTH = 185;
    private float POSTER_HEIGHT =  300;
    private float ASPECT_RATIO = POSTER_WIDTH/POSTER_HEIGHT;
    private ArrayList<Movie> moviesList;



    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey("moviesList")) {

            // ....if no state was saved
            moviesList = new ArrayList<Movie>();

        }
        else {
            moviesList = savedInstanceState.getParcelableArrayList("moviesList");
            filter = savedInstanceState.getString("filterId");
        }

        setContentView(R.layout.activity_main);

        // ....finding views
        popularityFilter = findViewById(R.id.sort_1);
        reviewFilter = findViewById(R.id.sort_2);
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

        if (moviesList.isEmpty()){
            updateActivity(filter);
        }else{
            recoverActivity();
        }




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
    public void onClick(Movie movie) {
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
        popularityFilter.setChecked(true);
        reviewFilter.setChecked(false);

        return true;
    }





    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        int id = item.getItemId();

        if (id == R.id.sort_1){

            setFilter(POPULARITY);
            updateActivity(filter);
            return true;

        }else if(id == R.id.sort_2){

            setFilter(REVIEW);
            updateActivity(filter);
            return true;

        }else{
            return super.onOptionsItemSelected(item);
        }


    }





    private void setFilter(String filterId) {


        switch (filterId) {

            case POPULARITY:
                popularityFilter.setChecked(true);
                reviewFilter.setChecked(false);
                filter = POPULARITY;
                break;



            case REVIEW:
                popularityFilter.setChecked(false);
                reviewFilter.setChecked(true);
                filter = REVIEW;
                break;


            default:break;
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

    private void updateActivity(String filter){


        errorMessage.setVisibility(View.INVISIBLE);
        movieRecycler.setVisibility(View.VISIBLE);


        MyAsyncTask FetchMovieData = new MyAsyncTask(MainActivity.this);
        FetchMovieData.execute(filter);


    }



    private void recoverActivity(){

        updateIndicator.setVisibility(View.INVISIBLE);
        showMoviesView();
        adapter.setMoviesList(moviesList);
        setFilter(filter);


    }


    @Override
    public void onSaveInstanceState( @NonNull Bundle outState) {
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




    private int numberOfColumns(){

        // get Display dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        //get Display orientation
        int orientation = getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            float columns = (width*2)/(height*ASPECT_RATIO);
            return Math.round(columns);
        }
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            float columns = (width*2)/(height*ASPECT_RATIO);
            return Math.round(columns);
        }

        return 1;
    }



}
