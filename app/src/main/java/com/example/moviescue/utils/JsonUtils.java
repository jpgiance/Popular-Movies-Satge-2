package com.example.moviescue.utils;

import com.example.moviescue.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final int MOVIES_PER_API_QUERY = 20;



/**
 * This method parses the json obtained from TMDB api and return a list of movies
 *
 * @param json          JSON type String from TMDB api requested
 *
 * @return List of Movie objects from JSON String
*/


    public static ArrayList<Movie> parseMovieListJson (String json){

        ArrayList<Movie> movieList = new ArrayList<>();      // creating List of Movie objects to be returned

        try {
            JSONObject movieQuery = new JSONObject(json);       // creating json object from string parameter
            JSONArray queryResults = movieQuery.getJSONArray("results");        // creating JSONArray where movies are contained
            JSONObject newMovie;

            // iterating over the JSONArray and initializing each movie object to be appended to movieList
            for (int i = 0; i < MOVIES_PER_API_QUERY; i++) {

                newMovie = queryResults.getJSONObject(i);


                Movie movie = new Movie(newMovie.optString("title"));
                movie.setReleaseDate(newMovie.optString("release_date"));
                movie.setOverview(newMovie.optString("overview"));
                movie.setImageLink(newMovie.optString("poster_path"));
                movie.setId((Integer) newMovie.get("id"));
                movie.setVoteAvg(JSONObject.numberToString((Number) newMovie.get("vote_average")));
                movie.setPopularity(JSONObject.numberToString((Number)newMovie.get("popularity")));

                movieList.add(movie);

            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return movieList;
    }

}
