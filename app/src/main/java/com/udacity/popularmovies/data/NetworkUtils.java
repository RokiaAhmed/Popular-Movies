package com.udacity.popularmovies.data;

import android.net.Uri;

import com.google.gson.Gson;
import com.udacity.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {

    final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    final static String API_KEY = "a56da9e6508fd2a64c867e76282ed9a5";
    final static String LANGUAGE = "en-US";


    final static String PARAM_LANGUAGE = "language";
    final static String PARAM_PAGE= "page";
    final static String PARAM_API_KEY= "api_key";

    final static String IMAGE_BASE_URL = " http://image.tmdb.org/t/p/";
    final static String IMAGE_SIZE = "w185";


    public static URL buildMovieUrl(String moviesListType) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(moviesListType)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .appendQueryParameter(PARAM_PAGE, String.valueOf(1))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String buildImageUrl(String imagePath) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(imagePath)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url.toString();
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

     public static ArrayList<Movie> getArrayFromJson(String json, String key) throws JSONException {
         JSONObject jsonObject = new JSONObject(json);
         JSONArray jsonArray = jsonObject.getJSONArray(key);
        ArrayList<Movie> list = new ArrayList<>();
        if (jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Gson gson = new Gson();
                list.add(gson.fromJson((jsonArray.getJSONObject(i)).toString(), Movie.class));
            }
        }
        return list;
    }
}
