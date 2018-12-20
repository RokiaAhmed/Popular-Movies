package com.udacity.popularmovies.utilities;

import android.content.Context;
import android.content.Intent;

import com.udacity.popularmovies.MovieDetailsActivity;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.ReviewDetailsActivity;


/**
 * Created by mohamedzamel on 14/01/18.
 */

public class IntentTools {


    public static void goToMovieDetailsFromItem(Context context , int movieId){
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        String key = context.getString(R.string.key_item_id);
        intent.putExtra(key, movieId);
        context.startActivity(intent);
    }

    public static void goToReviewDetailsFromItem(Context context , int index){
        Intent intent = new Intent(context, ReviewDetailsActivity.class);
        String key = context.getString(R.string.key_item_id);
        intent.putExtra(key, index);
//        ((Activity) context).finish();
        context.startActivity(intent);
    }

}


