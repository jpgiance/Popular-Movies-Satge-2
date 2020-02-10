package com.example.moviescue.utils;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;


public class DetailActivityAsyncTask extends AsyncTask<Object, Void, Object> {

    private OnTaskCompleted activityContext;



    public DetailActivityAsyncTask(OnTaskCompleted context) {

        this.activityContext = context;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }




    @Override
    protected Object doInBackground( Object... objects ) {

        if (objects == null) {
            return null;
        }

        ArrayList<String> response = new ArrayList<>();

        URL trailersQuery = (URL) objects[0];
        URL reviewsQuery = (URL) objects[1];

        try {
            response.add(NetworkUtils.getResponseFromHttpUrl(trailersQuery));
            response.add(NetworkUtils.getResponseFromHttpUrl(reviewsQuery));

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onPostExecute( Object response) {


        activityContext.onTaskCompleted((ArrayList<String>)response);
    }





    /**
     * This interface can be implemented in the activity for handling
     * response and pre-execution task
     *
     */

    public interface OnTaskCompleted {
        void onTaskCompleted(ArrayList<String> response);
        void preExecute();
    }

}



