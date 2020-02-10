package com.example.moviescue.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String title;
    private String releaseDate;
    private String overview;
    private String imageLink;
    private String voteAvg;
    private String popularity;
    private String reviewsJSON;    // This variable is a JSON that contains all available reviews
    private String trailersJSON;  // This variable is a JSON that contains all available trailersJSON
    private Integer id;





    public Movie(String movieTitle){
        this.title = movieTitle;
    }




    public void setReleaseDate( String date){

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

    public void setId( Integer id ) {

        this.id = id;
    }

    public void setReviewsJSON( String reviews ) {
        this.reviewsJSON = reviews;
    }

    public void setTrailersJSON( String trailersJSON ) {
        this.trailersJSON = trailersJSON;
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

    public Integer getId() { return id; }

    public String getReviewsJSON() { return reviewsJSON; }

    public String getTrailersJSON() { return trailersJSON;}





    protected Movie( Parcel in ) {
        title = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        imageLink = in.readString();
        voteAvg = in.readString();
        popularity = in.readString();
        id = in.readInt();
        trailersJSON = in.readString();
        reviewsJSON = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel( Parcel in ) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray( int size ) {
            return new Movie[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags ) {
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeString(imageLink);
        dest.writeString(voteAvg);
        dest.writeString(popularity);
        dest.writeInt(id);
        dest.writeString(trailersJSON);
        dest.writeString(reviewsJSON);
    }
}


