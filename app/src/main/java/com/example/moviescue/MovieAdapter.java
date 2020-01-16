package com.example.moviescue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviescue.model.Movie;
import com.example.moviescue.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {


    private Context ctx;
    private List<Movie> moviesList;
    private final MovieAdapterOnClickHandler mClickHandler;





    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }





    public MovieAdapter( Context context, MovieAdapterOnClickHandler clickHandler ){

      ctx = context;
      mClickHandler = clickHandler;

    }






    public class MovieHolder extends RecyclerView.ViewHolder implements OnClickListener {

        ImageView movieImage1;
        ImageView movieImage2;



        public MovieHolder( View itemView){

            super(itemView);

            movieImage1 = itemView.findViewById(R.id.image_1);
            movieImage2 = itemView.findViewById(R.id.image_2);


            movieImage1.setOnClickListener(this);
            movieImage2.setOnClickListener(this);

        }





        @Override
        public void onClick( View v ) {

            int adapterPosition = getAdapterPosition();
            if (v.getId() == movieImage1.getId()){

                mClickHandler.onClick(moviesList.get(adapterPosition*2));

            }
            else if(v.getId() == movieImage2.getId()){

                mClickHandler.onClick(moviesList.get(adapterPosition*2 + 1));
            }else {
                Log.d("Recycler items onClick ", "Item ID not recognized");}


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
                .load(loadMoviePoster(position*2))
                .placeholder(R.mipmap.ic_launcher)             // placeholder could be improved!!
                .into(holder.movieImage1);


        Picasso.get()
                .load(loadMoviePoster(position*2 + 1))
                .placeholder(R.mipmap.ic_launcher)            // placeholder could be improved!!
                .into(holder.movieImage2);


    }





    @Override
    public int getItemCount(){
        if (null == moviesList) {
            return 0;
        }
        return moviesList.size()/2;

    }






    /**
     * This method update Movies List state
     *
     * @param movies
     */
    public void setMoviesList(List<Movie> movies) {
        moviesList = movies;
        notifyDataSetChanged();
    }






    public String loadMoviePoster(int pos){

        return ( NetworkUtils.BASE_POSTER_PATH + NetworkUtils.SIZE_185 + this.moviesList.get(pos).getImageLink());
    }


}

