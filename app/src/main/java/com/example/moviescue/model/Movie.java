package com.example.moviescue.model;

import java.io.Serializable;

public class Movie implements Serializable {

    private String title;
    private String releaseDate;
    private String overview;
    private String imageLink;
    private String voteAvg;
    private String popularity;




    public Movie(String movieTitle){
        this.title = movieTitle;
    }





    public void setReleaseDate(String date){

        this.releaseDate = date;
    }

    public void setOverview(String summary){

        this.overview = summary;
    }

    public void setImageLink(String link){

        this.imageLink = link;
    }

    public void setVoteAvg(String vote){

        this.voteAvg = vote;
    }

    public void setPopularity(String popular){

        this.popularity = popular;
    }



    public String getImageLink(){
        return this.imageLink;
    }

    public String getTitle(){
        return this.title;
    }

    public String getReleaseDate(){
        return this.releaseDate;
    }

    public String getOverview(){
        return this.overview;
    }

    public String getVoteAvg(){
        return this.voteAvg;
    }


}


