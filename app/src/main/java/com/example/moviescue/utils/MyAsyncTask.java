package com.example.moviescue.utils;

import android.os.AsyncTask;

import java.net.URL;

public class MyAsyncTask extends AsyncTask<Object, Void, String> {

    private OnTaskCompleted activityContext;




    /**
     * constructor assigns activity context
     * to local variable for pre/post execution task
     *
     */

    public MyAsyncTask(OnTaskCompleted context) {
        this.activityContext = context;
    }





    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        activityContext.preExecute();

    }




    @Override
    protected String doInBackground(Object[] objects) {

        if (objects == null) {
            return null;
        }

        URL apiQuery = (URL) objects[0];

        try {
            return NetworkUtils.getResponseFromHttpUrl(apiQuery);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





    @Override
    protected void onPostExecute( String queryResponse ) {

        activityContext.onTaskCompleted(queryResponse);
    }




/**
 * This interface can be implemented in the activity for handling
 * response and pre-execution task
 *
 */

    public interface OnTaskCompleted {
        void onTaskCompleted(String response);
        void preExecute();
    }


}
