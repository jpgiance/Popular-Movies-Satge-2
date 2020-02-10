package com.example.moviescue.utils;

import android.net.Uri;
import android.util.Log;

import com.example.moviescue.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String BASE_POSTER_PATH = "https://image.tmdb.org/t/p/";
    public static final String BASE_TRAILER_VIDEO_PATH = "https://www.youtube.com/watch?v=";    //"https://www.youtube.com/embed/";
    public static final String BASE_TRAILER_PICTURE_PATH = "https://img.youtube.com/vi/";
    public static final String ENDING_TRAILER_PICTURE_PATH = "/0.jpg";
    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_QUERY_SORT_ = "?sort_by=";
    public static final String REVIEWS = "/reviews?";
    public static final String TRAILERS = "/videos?";
    public static final String API_KEY_VALUE = BuildConfig.YOUR_API_KEY;
    public static final String SIZE_92 = "w92";
    public static final String SIZE_154 = "w154";
    public static final String SIZE_185 = "w185/";
    public static final String SIZE_342 = "w342";
    public static final String SIZE_500 = "w500";
    public static final String SIZE_780 = "w780";
    public static final String SIZE_ORIGINAL = "original";
    public static final String SORT_OPTION_1 = "popular?";
    public static final String SORT_OPTION_2 = "top_rated?";
    public static final String LIST_SORT_ASCENDING = "asc";
    public static final String LIST_SORT_DESCENDING = "desc";
    public static final String API_QUERY_KEY = "api_key=";






    /**
     * This method takes a String that encodes the filter selected by the user and
     * builds the URL ready to be use in the API query
     *
     * @param sortPreference encodes the filter selected by the user
     *
     * @return URL to be used for TMDB API requests
     */

    public static URL buildUrl(String sortPreference) {

        URL url = null;

        switch (sortPreference) {

            case "popularity": {
                Uri builtUri = Uri.parse(TMDB_BASE_URL
                        + SORT_OPTION_1
                        + API_QUERY_KEY
                        + API_KEY_VALUE)
                        .buildUpon()
                        .build();

                try {
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Built URI " + url);
                return url;

            }
            case "review": {
                Uri builtUri = Uri.parse(TMDB_BASE_URL
                        + SORT_OPTION_2
                        + API_QUERY_KEY
                        + API_KEY_VALUE)
                        .buildUpon()
                        .build();
                try {
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Log.v(TAG, "Built URI " + url);
                return url;

            }

            default:
                return null;

        }


    }


    public static URL buildReviewsUrl(String id){

        URL url = null;

        Uri builtUri = Uri.parse(TMDB_BASE_URL
                + id
                + REVIEWS
                + API_QUERY_KEY
                + API_KEY_VALUE)
                .buildUpon()
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Built URI " + url);
        return url;

    }


    public static URL buildTrailersUrl(String id){

        URL url = null;

        Uri builtUri = Uri.parse(TMDB_BASE_URL
                + id
                + TRAILERS
                + API_QUERY_KEY
                + API_KEY_VALUE)
                .buildUpon()
                .build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Built URI " + url);
        return url;

    }

    public static String movieTrailerPicturePath(String key){

        return BASE_TRAILER_PICTURE_PATH + key + ENDING_TRAILER_PICTURE_PATH;
    }


    public static String movieTrailerVideoPath(String key){

        return BASE_TRAILER_VIDEO_PATH + key;
    }








    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }



}
