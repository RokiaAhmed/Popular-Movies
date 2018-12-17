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
import com.udacity.popularmovies.model.MovieTrailer;

import java.util.ArrayList;

class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    ArrayList<MovieTrailer> mTrailerList;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public TrailerAdapter(ArrayList list, ListItemClickListener listener) {
        mTrailerList = list;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_trailer, viewGroup, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.trailerTitleTextView.setText(mTrailerList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public ImageView playImageView;
        TextView trailerTitleTextView;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            playImageView = itemView.findViewById(R.id.iv_play);
            trailerTitleTextView = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
