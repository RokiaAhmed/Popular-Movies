package com.udacity.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.data.NetworkUtils;
import com.udacity.popularmovies.model.Movie;

 import java.util.ArrayList;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    ArrayList<Movie> mMoviesList;

    public MoviesAdapter(ArrayList list) {
        mMoviesList = list;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        String imageUrl = NetworkUtils.buildImageUrl(mMoviesList.get(position).getPoster_path());
        Picasso.get().load(imageUrl).into(holder.movieImage);

    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieImage;
        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.image_movie);
        }
    }
}
