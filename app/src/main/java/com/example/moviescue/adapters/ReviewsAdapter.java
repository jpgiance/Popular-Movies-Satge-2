package com.example.moviescue.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviescue.R;
import com.example.moviescue.model.MovieReview;

import java.util.ArrayList;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder> {


    private Context ctx;
    private ArrayList<MovieReview> reviewsList;


    public ReviewsAdapter( Context ctx ) {
        this.ctx = ctx;
    }


    public class ReviewsHolder extends RecyclerView.ViewHolder {


        TextView reviewAuthor;
        TextView reviewText;


        public ReviewsHolder( View itemView ) {

            super(itemView);

            reviewText = itemView.findViewById(R.id.review);
            reviewAuthor = itemView.findViewById(R.id.review_author);


        }


    }


    @NonNull
    @Override
    public ReviewsAdapter.ReviewsHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {

        LayoutInflater reviewsInflater = LayoutInflater.from(ctx);
        View trailerView = reviewsInflater.inflate(R.layout.reviews_item, parent, false);
        ReviewsHolder holder = new ReviewsHolder(trailerView);
        return holder;
    }

    @Override
    public void onBindViewHolder( @NonNull ReviewsAdapter.ReviewsHolder holder, int position ) {

        holder.reviewAuthor.setText(reviewsList.get(position).getAuthor());
        holder.reviewText.setText(reviewsList.get(position).getReviewText());
    }

    @Override
    public int getItemCount() {
        if (null == reviewsList) {
            return 0;
        }
        return reviewsList.size();
    }


    public void setReviewsList( ArrayList<MovieReview> reviews ) {

        reviewsList = reviews;
        notifyDataSetChanged();

    }
}



