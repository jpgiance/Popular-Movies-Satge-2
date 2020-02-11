package com.example.moviescue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviescue.adapters.ReviewsAdapter;
import com.example.moviescue.adapters.TrailersAdapter;
import com.example.moviescue.database.MovieDatabase;
import com.example.moviescue.executor.AppExecutors;
import com.example.moviescue.model.Movie;
import com.example.moviescue.model.MovieReview;
import com.example.moviescue.model.MovieTrailer;
import com.example.moviescue.utils.DetailActivityAsyncTask;
import com.example.moviescue.utils.JsonUtils;
import com.example.moviescue.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;


public class MovieDetail extends AppCompatActivity implements DetailActivityAsyncTask.OnTaskCompleted {

    private Movie detailMovie;
    private TextView title;
    private TextView year;
    private TextView vote;
    private TextView overview;
    private ImageView poster;
    private ImageButton favoriteIcon;

    private String YEAR_ERROR = "Release year is not available";
    private String OVERVIEW_ERROR = "Movie overview is not available";
    private Boolean isFavoriteCheckFinish = false;

    private ArrayList<MovieReview> reviewsList;
    private ArrayList<MovieTrailer> trailersList;
    private TrailersAdapter trailersAdapter;
    private RecyclerView trailersRecycler;
    private RecyclerView reviewsRecycler;
    private ReviewsAdapter reviewsAdapter;


    private boolean isFavorite = false;

    private MovieDatabase movieDb;


    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie);


        movieDb = MovieDatabase.getInstance(getApplicationContext());

        // ....finding views
        title = findViewById(R.id.movie_title);
        year = findViewById(R.id.movie_year);
        vote = findViewById(R.id.movie_avg_vote);
        overview = findViewById(R.id.movie_overview);
        poster = findViewById(R.id.movie_poster);
        trailersRecycler = findViewById(R.id.trailer_recycler);
        reviewsRecycler = findViewById(R.id.review_recycler);
        favoriteIcon = findViewById(R.id.button);


        // ....setting up trailersAdapter
        trailersAdapter = new TrailersAdapter(this);
        trailersRecycler.setAdapter(trailersAdapter);
        trailersRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trailersRecycler.setHasFixedSize(true);


        // ....setting up reviewsAdapter
        reviewsAdapter = new ReviewsAdapter(this);
        reviewsRecycler.setAdapter(reviewsAdapter);
        reviewsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewsRecycler.setHasFixedSize(true);


        // ....getting intent from previous Activity
        Intent detailIntent = getIntent();
        populateDetailActivity(detailIntent);


    }


    private void populateDetailActivity( Intent detailIntent ) {

        if (detailIntent != null) {
            if (detailIntent.hasExtra("movie")) {

                // ....getting object <movie> from intent
                detailMovie = (Movie) detailIntent.getParcelableExtra("movie");


                checkIfFavorite();

                // ....resizing the title if to large
                if (detailMovie.getTitle().length() > 14) {
                    title.setTextSize(30);
                    title.setText(detailMovie.getTitle());
                } else {
                    title.setTextSize(55);
                    title.setText(detailMovie.getTitle());
                }


                // ....populating detail screen
                if (!detailMovie.getReleaseDate().equals("")) {
                    year.setText(detailMovie.getReleaseDate().substring(0, 4));
                } else {
                    year.setTextSize(14);
                    year.setText(YEAR_ERROR);
                }

                title.setText(detailMovie.getTitle());
                overview.setText(detailMovie.getOverview());
                vote.setText(detailMovie.getVoteAvg());

                Picasso.get()
                        .load(NetworkUtils.BASE_POSTER_PATH + NetworkUtils.SIZE_185 + detailMovie.getImageLink())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(poster);

                loadAdditionalMovieData(detailMovie.getId());

            } else {
                Log.d("Activity main to detail", "Intent has no attachment");
            }
        } else {
            Log.d("Activity main to detail", "Intent is null");
        }
    }

    private void checkIfFavorite() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                Movie movieHolder = movieDb.movieDao().retrieveMovieId(detailMovie.getId());
                if (movieHolder != null) {

                    detailMovie = movieHolder;
                    isFavorite = true;
                    isFavoriteCheckFinish = true;
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_red);


                } else {
                    isFavorite = false;
                    isFavoriteCheckFinish = true;
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_grey);

                }

            }
        });


    }


    public void addToFavorite( View view ) {


        if (!isFavorite) {

            isFavorite = true;
            favoriteIcon.setImageResource(R.drawable.ic_favorite_red);

            final Movie movieFavorite = detailMovie;
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    movieDb.movieDao().insertTask(movieFavorite);

                }
            });
        } else {

            favoriteIcon.setImageResource(R.drawable.ic_favorite_grey);
            isFavorite = false;
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    movieDb.movieDao().deleteTask(detailMovie);
                }
            });

        }
    }


    private void loadAdditionalMovieData( Integer id ) {
        while (!isFavoriteCheckFinish) {
        }


        if (isFavorite) {

            trailersList = JsonUtils.parseTrailersList(detailMovie.getTrailersJSON());
            reviewsList = JsonUtils.parseReviewsList(detailMovie.getReviewsJSON());

            trailersAdapter.setTrailersList(trailersList);
            reviewsAdapter.setReviewsList(reviewsList);

        } else {

            URL queryTrailersUrl = NetworkUtils.buildTrailersUrl(id.toString());
            URL queryReviewsUrl = NetworkUtils.buildReviewsUrl(id.toString());

            DetailActivityAsyncTask loadAdditionalData = new DetailActivityAsyncTask(MovieDetail.this);
            loadAdditionalData.execute(queryTrailersUrl, queryReviewsUrl);

        }


    }


    @Override
    public void onTaskCompleted( ArrayList<String> response ) {

        if (response != null) {
            detailMovie.setTrailersJSON(response.get(0));
            detailMovie.setReviewsJSON(response.get(1));

            trailersList = JsonUtils.parseTrailersList(response.get(0));
            reviewsList = JsonUtils.parseReviewsList(response.get(1));

            trailersAdapter.setTrailersList(trailersList);
            reviewsAdapter.setReviewsList(reviewsList);
        }
    }

    @Override
    public void preExecute() {

        return;
    }
}


