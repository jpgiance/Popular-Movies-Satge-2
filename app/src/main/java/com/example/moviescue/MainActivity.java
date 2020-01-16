package com.example.moviescue;

import com.example.moviescue.model.Movie;
import com.example.moviescue.utils.JsonUtils;
import com.example.moviescue.utils.MyAsyncTask;
import com.example.moviescue.utils.NetworkUtils;
import com.example.moviescue.MovieAdapter.MovieAdapterOnClickHandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
        movieRecycler.setLayoutManager(new LinearLayoutManager(
                                                    this,
                                                            LinearLayoutManager.VERTICAL,
                                                false));
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


   /**


    /**
     * This class provide the methods that allow Async Task for http communication
     *
     *

    public class FetchMovieData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            updateIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground( String... params ) {

            if (params.length == 0) {
                return null;
            }

            URL apiQuery = NetworkUtils.buildUrl(params[0]);

            try {
                return NetworkUtils.getResponseFromHttpUrl(apiQuery);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute( String queryResponse ) {
            updateIndicator.setVisibility(View.INVISIBLE);
            if (queryResponse != null) {
                showMoviesView();
                adapter.setMoviesList(JsonUtils.parseMovieListJson(queryResponse));
            } else {
                showErrorMessage();
            }
        }
    }

    */

}
