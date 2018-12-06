package com.udacity.popularmovies.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.udacity.popularmovies.MovieDetailsActivity;
import com.udacity.popularmovies.R;


/**
 * Created by mohamedzamel on 14/01/18.
 */

public class IntentTools {


    public static void goToMovieDetailsFromItem(Context context , int index){
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        String key = context.getString(R.string.key_item_clicked_index);
        intent.putExtra(key, index);
        ((Activity) context).finish();
        context.startActivity(intent);
    }

    public static void goToLogin(Context context){
//        Intent intent = new Intent(context, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//        ((Activity) context).finish();
//        context.startActivity(intent);
    }
}
//    public static void goToActivityFromNotificationWithData(Context context, Class aClass, Bundle bundle){
//        Intent intent = new Intent(context, aClass);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }}


