package com.example.moviescue.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviescue.R;
import com.example.moviescue.model.MovieTrailer;
import com.example.moviescue.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerHolder> {

    private Context ctx;
    private ArrayList<MovieTrailer> trailersList;


    public TrailersAdapter( Context context ) {
        ctx = context;
    }


    public class TrailerHolder extends RecyclerView.ViewHolder implements OnClickListener {

        ImageView trailerImage;

        public TrailerHolder( View itemView ) {

            super(itemView);

            trailerImage = itemView.findViewById(R.id.image_1);
            trailerImage.setOnClickListener(this);

        }

        @Override
        public void onClick( View v ) {

            int adapterPosition = getAdapterPosition();


            String trailerVideoPath = NetworkUtils.movieTrailerVideoPath(trailersList.get(adapterPosition).getVideoKey());
            Uri trailerVideoUri = Uri.parse(trailerVideoPath);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(trailerVideoUri);

            if (true && intent.resolveActivity(ctx.getPackageManager()) != null) {
                ctx.startActivity(intent);
            } else {

                Toast.makeText(ctx,
                        "Video format is not supported",
                        Toast.LENGTH_LONG)
                        .show();

            }


        }
    }


    @NonNull
    @Override
    public TrailersAdapter.TrailerHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {

        LayoutInflater trailerInflater = LayoutInflater.from(ctx);
        View trailerView = trailerInflater.inflate(R.layout.trailer_item, parent, false);
        TrailerHolder holder = new TrailerHolder(trailerView);
        return holder;
    }


    @Override
    public void onBindViewHolder( @NonNull TrailersAdapter.TrailerHolder holder, int position ) {
        String trailerPicturePath = NetworkUtils.movieTrailerPicturePath(trailersList.get(position).getVideoKey());
        Picasso.get()
                .load(trailerPicturePath)
                .placeholder(R.mipmap.ic_launcher)             // placeholder could be improved!!
                .into(holder.trailerImage);
    }

    @Override
    public int getItemCount() {
        if (null == trailersList) {
            return 0;
        }
        return trailersList.size();
    }


    public void setTrailersList( ArrayList<MovieTrailer> trailers ) {

        trailersList = trailers;
        notifyDataSetChanged();

    }
}
