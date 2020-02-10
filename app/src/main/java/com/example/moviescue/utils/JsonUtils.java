package com.example.moviescue.utils;

import com.example.moviescue.model.Movie;
import com.example.moviescue.model.MovieReview;
import com.example.moviescue.model.MovieTrailer;

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
            for (int i = 0; i < queryResults.length(); i++) {

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






    public static ArrayList<MovieTrailer>  parseTrailersList( String json){



        ArrayList<MovieTrailer> movieTrailersList = new ArrayList<>();

        try {
            JSONObject trailerQuery = new JSONObject(json);       // creating json object from string parameter
            JSONArray queryResults = trailerQuery.getJSONArray("results");        // creating JSONArray where moviesTrailers are contained
            JSONObject newMovieTrailer;

            // iterating over the JSONArray and initializing each movieTrailer object to be appended to movieTrailerList
            for (int i = 0; i < queryResults.length(); i++) {

                newMovieTrailer = queryResults.getJSONObject(i);


                MovieTrailer trailer = new MovieTrailer();
                trailer.setSite(newMovieTrailer.optString("site"));
                trailer.setVideoKey(newMovieTrailer.optString("key"));

                movieTrailersList.add(trailer);

            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return movieTrailersList;

    }





    public static ArrayList<MovieReview>  parseReviewsList( String json){

        ArrayList<MovieReview> movieReviewsList = new ArrayList<>();

        try {
            JSONObject reviewQuery = new JSONObject(json);       // creating json object from string parameter
            JSONArray queryResults = reviewQuery.getJSONArray("results");        // creating JSONArray where moviesReviews are contained
            JSONObject newMovieReview;

            // iterating over the JSONArray and initializing each movieReview object to be appended to movieReviewsList
            for (int i = 0; i < queryResults.length(); i++) {

                newMovieReview = queryResults.getJSONObject(i);


                MovieReview review = new MovieReview();
                review.setAuthor(newMovieReview.optString("author"));
                review.setReviewText(newMovieReview.optString("content"));

                movieReviewsList.add(review);

            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return movieReviewsList;


    }


}




