package com.example.moviescue.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviescue.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder>{


    private Context ctx;
    private ArrayList<String> reviewsList;


    public ReviewsAdapter( Context ctx ) {
        this.ctx = ctx;
    }


    public class ReviewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{



        ImageView reviewImage;


        public ReviewsHolder(View itemView){

            super(itemView);

            reviewImage = itemView.findViewById(R.id.image_1);

            reviewImage.setOnClickListener(this);

        }

        @Override
        public void onClick( View v ) {

            int adapterPosition = getAdapterPosition();
            Toast.makeText(ctx, "this pressed", Toast.LENGTH_SHORT).show();

        }
    }





    @NonNull
    @Override
    public ReviewsAdapter.ReviewsHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {

        LayoutInflater reviewsInflater = LayoutInflater.from(ctx);
        View trailerView = reviewsInflater.inflate(R.layout.trailer_item, parent, false);
        ReviewsHolder holder = new ReviewsHolder(trailerView);
        return holder;
    }

    @Override
    public void onBindViewHolder( @NonNull ReviewsAdapter.ReviewsHolder holder, int position ) {

        Picasso.get()
                .load(R.drawable.chappie_big)
                .placeholder(R.mipmap.ic_launcher)             // placeholder could be improved!!
                .into(holder.reviewImage);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
