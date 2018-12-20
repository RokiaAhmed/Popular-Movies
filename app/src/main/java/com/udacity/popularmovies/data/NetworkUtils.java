package com.udacity.popularmovies.data;

import android.net.Uri;

import com.google.gson.Gson;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieReview;
import com.udacity.popularmovies.model.MovieTrailer;

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
    final static String PARAM_PAGE = "page";
    final static String PARAM_API_KEY = "api_key";

    final static String IMAGE_BASE_URL = " http://image.tmdb.org/t/p/";
    final public static String IMAGE_SIZE_MAIN = "w185";
    final public static String IMAGE_SIZE_DETAILS = "w780";


    final static String PARAM_VIDEOS = "videos";
    final static String PARAM_REVIEWS = "reviews";

    public static URL buildMovieUrl(String moviesListType) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(moviesListType)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .appendQueryParameter(PARAM_PAGE, String.valueOf(1))
                .build();

        return buildUrl(builtUri);
    }

    public static URL buildMovieDetailsUrl(int movieID) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieID))
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .build();
        return buildUrl(builtUri);
    }

    public static URL buildMovieTrailerUrl(int movieID) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieID))
                .appendPath(PARAM_VIDEOS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .build();
        return buildUrl(builtUri);
    }

    public static URL buildMovieReviewUrl(int movieID) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(movieID))
                .appendPath(PARAM_REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .build();
        return buildUrl(builtUri);
    }

    private static URL buildUrl(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String buildImageUrl(String imagePath, String imageSize) {
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(imageSize)
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

    public static Movie getMovie(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        Movie movie = new Movie();
        movie.setOriginal_title(jsonObject.getString("original_title"));
        movie.setRelease_date(jsonObject.getString("release_date"));
        movie.setPoster_path(jsonObject.getString("poster_path"));
        movie.setVote_average((float) jsonObject.getDouble("vote_average"));
        movie.setOverview(jsonObject.getString("overview"));
        return movie;
    }

    public static ArrayList<MovieTrailer> getMovieTrailer (String json, String key) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(key);
        ArrayList<MovieTrailer> list = new ArrayList<>();
        if (jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Gson gson = new Gson();
                list.add(gson.fromJson((jsonArray.getJSONObject(i)).toString(), MovieTrailer.class));
            }
        }
        return list;
    }

    public static ArrayList<MovieReview> getMovieReviews (String json, String key) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(key);
        ArrayList<MovieReview> list = new ArrayList<>();
        if (jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Gson gson = new Gson();
                list.add(gson.fromJson((jsonArray.getJSONObject(i)).toString(), MovieReview.class));
            }
        }
        return list;
    }
}
