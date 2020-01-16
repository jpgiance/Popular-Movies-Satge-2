package com.example.moviescue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviescue.model.Movie;
import com.example.moviescue.utils.NetworkUtils;
import com.squareup.picasso.Picasso;


public class MovieDetail extends AppCompatActivity {

   private Movie detailMovie;
   private TextView title;
    private TextView year;
    private TextView vote;
    private TextView overview;
    private ImageView poster;

    private String YEAR_ERROR = "Release year is not available";
    private String OVERVIEW_ERROR = "Movie overview is not available";

    @Override
    public void onCreate(  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie);


        // ....finding views
        title = findViewById(R.id.movie_title);
        year = findViewById(R.id.movie_year);
        vote = findViewById(R.id.movie_avg_vote);
        overview = findViewById(R.id.movie_overview);
        poster = findViewById(R.id.movie_poster);




        // ....getting intent from previous Activity
        Intent detailIntent = getIntent();

        if (detailIntent != null) {
            if (detailIntent.hasExtra("movie")) {

                // ....getting object <movie> from intent
                detailMovie = (Movie) detailIntent.getSerializableExtra("movie");


                // ....resizing the title if to large
                if (detailMovie.getTitle().length() > 14 ){
                    title.setTextSize(30);
                    title.setText(detailMovie.getTitle());
                }else{
                    title.setTextSize(55);
                    title.setText(detailMovie.getTitle());
                }


                // ....populating detail screen
                if(!detailMovie.getReleaseDate().equals("")){
                    year.setText(detailMovie.getReleaseDate().substring(0, 4));
                }else {
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
            }
            else{
                Log.d("Activity main to detail", "Intent has no attachment");
            }
        }
        else{
            Log.d("Activity main to detail", "Intent is null");
        }


    }
}
