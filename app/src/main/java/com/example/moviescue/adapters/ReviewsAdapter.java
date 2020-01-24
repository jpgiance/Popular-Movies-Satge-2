package com.example.moviescue.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;





public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder>{


    public class ReviewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public ReviewsHolder(View itemView){

            super(itemView);

        }

        @Override
        public void onClick( View v ) {

        }
    }





    @NonNull
    @Override
    public ReviewsAdapter.ReviewsHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        return null;
    }

    @Override
    public void onBindViewHolder( @NonNull ReviewsAdapter.ReviewsHolder holder, int position ) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
