package com.example.moviescue;

import com.example.moviescue.model.Movie;
import com.example.moviescue.utils.JsonUtils;
import com.example.moviescue.utils.MyAsyncTask;
import com.example.moviescue.utils.NetworkUtils;
import com.example.moviescue.MovieAdapter.MovieAdapterOnClickHandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.DisplayMetrics;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.net.URL;
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

    private String POPULARITY = "popularity";
    private String REVIEW = "review";
    private String filter = POPULARITY;        // By default filter popularity is used
    private float POSTER_WIDTH = 185;
    private float POSTER_HEIGHT =  300;
    private  float ASPECT_RATIO = POSTER_WIDTH/POSTER_HEIGHT;



    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
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


        // ....fetching data and populating main screen
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



        switch (id){

            case R.id.sort_1:
                popularityFilter.setChecked(true);
                reviewFilter.setChecked(false);
                setFilter(POPULARITY);
                updateActivity(filter);
                return true;


            case R.id.sort_2:
                popularityFilter.setChecked(false);
                reviewFilter.setChecked(true);
                setFilter(REVIEW);
                updateActivity(filter);
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }

    }





    private void setFilter(String filterSelected){

        filter = filterSelected;
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






    @Override
    public void preExecute() {

        updateIndicator.setVisibility(View.VISIBLE);
    }




    @Override
    public void onTaskCompleted( String queryResponse ) {

        updateIndicator.setVisibility(View.INVISIBLE);
        if (queryResponse != null) {
            showMoviesView();
            adapter.setMoviesList(JsonUtils.parseMovieListJson(queryResponse));
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
