package com.example.moviescue.model;

public class MovieReview {

    private String author;
    private String reviewText;


    public MovieReview() {
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor( String author ) {
        this.author = author;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText( String reviewText ) {
        this.reviewText = reviewText;
    }
}
