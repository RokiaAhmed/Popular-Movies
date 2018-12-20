package com.udacity.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies.model.MovieReview;

import java.util.ArrayList;

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    ArrayList<MovieReview> mReviewList;
    final private ListReviewItemClickListener mOnClickListener;

    public interface ListReviewItemClickListener {
        void onListReviewItemClick(int clickedItemIndex);
    }

    public ReviewsAdapter(ArrayList list, ListReviewItemClickListener listener) {
        mReviewList = list;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_review, viewGroup, false);
        ReviewsViewHolder viewHolder = new ReviewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        holder.userNameTextView.setText(mReviewList.get(position).getAuthor());
        holder.contentTextView.setText(mReviewList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
         TextView userNameTextView, contentTextView;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
             userNameTextView = itemView.findViewById(R.id.tv_user_name);
            contentTextView = itemView.findViewById(R.id.tv_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListReviewItemClick(clickedPosition);
        }
    }
}
