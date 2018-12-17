package com.udacity.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.data.NetworkUtils;
import com.udacity.popularmovies.model.Movie;

import java.util.ArrayList;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    ArrayList<Movie> mMoviesList;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MoviesAdapter(ListItemClickListener listener) {
//        mMoviesList = movieList;
        mOnClickListener = listener;
    }

    public void setList(ArrayList list) {
        mMoviesList = list;
        notifyDataSetChanged();
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
        String imageUrl = NetworkUtils.buildImageUrl(mMoviesList.get(position).getPoster_path(),
                NetworkUtils.IMAGE_SIZE_MAIN);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder)
                .into(holder.movieImage);
        holder.movieTitleTextView.setText(mMoviesList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public ImageView movieImage;
        TextView movieTitleTextView;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.image_movie);
            movieTitleTextView = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
