package com.example.moviescue.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder>{



    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TrailerHolder(View itemView){

            super(itemView);

        }

        @Override
        public void onClick( View v ) {

        }
    }




    @NonNull
    @Override
    public TrailerAdapter.TrailerHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        return null;
    }

    @Override
    public void onBindViewHolder( @NonNull TrailerAdapter.TrailerHolder holder, int position ) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
