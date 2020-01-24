package com.example.moviescue.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviescue.R;
import com.example.moviescue.model.Movie;
import com.example.moviescue.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {


    private Context ctx;
    private ArrayList<Movie> moviesList;
    private final MovieAdapterOnClickHandler mClickHandler;





    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }





    public MovieAdapter( Context context, MovieAdapterOnClickHandler clickHandler ){

      ctx = context;
      mClickHandler = clickHandler;

    }






    public class MovieHolder extends RecyclerView.ViewHolder implements OnClickListener  {

        ImageView movieImage1;



        public MovieHolder( View itemView){

            super(itemView);

            movieImage1 = itemView.findViewById(R.id.image_1);

            movieImage1.setOnClickListener(this);

        }





        @Override
        public void onClick( View v ) {

            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(moviesList.get(adapterPosition));


        }
    }





    @NonNull
    @Override
    public MovieHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {

        LayoutInflater movieInflater = LayoutInflater.from(ctx);
        View movieView = movieInflater.inflate(R.layout.movie_item, parent, false);
        movieView.getLayoutParams().height = parent.getHeight()/2;
        MovieHolder holder = new MovieHolder(movieView);

        return holder;
    }






    @Override
    public void onBindViewHolder( @NonNull MovieAdapter.MovieHolder holder, int position ) {


        Picasso.get()
                .load(loadMoviePoster(position))
                .placeholder(R.mipmap.ic_launcher)             // placeholder could be improved!!
                .into(holder.movieImage1);


    }





    @Override
    public int getItemCount(){
        if (null == moviesList) {
            return 0;
        }
        return moviesList.size();

    }






    /**
     * This method update Movies List state
     *
     * @param movies
     */
    public void setMoviesList(ArrayList<Movie> movies) {
        moviesList = movies;
        notifyDataSetChanged();
    }






    public String loadMoviePoster(int pos){

        return ( NetworkUtils.BASE_POSTER_PATH + NetworkUtils.SIZE_185 + this.moviesList.get(pos).getImageLink());
    }


}

