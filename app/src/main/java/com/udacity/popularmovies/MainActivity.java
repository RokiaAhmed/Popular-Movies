package com.udacity.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.popularmovies.data.NetworkUtils;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utilities.ConnectionDetector;
import com.udacity.popularmovies.utilities.IntentTools;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    private RecyclerView mMoviesList;
    private ProgressBar mProgressbarLoading;
    private TextView noConnectionTextView;
    private MoviesAdapter mAdapter;
    private ConnectionDetector connectionDetector;
    private ArrayList<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressbarLoading = findViewById(R.id.progress_bar_loading);
        noConnectionTextView = findViewById(R.id.tv_no_connection);
        mMoviesList = findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mMoviesList.setLayoutManager(gridLayoutManager);
        mMoviesList.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(this);
        mMoviesList.setAdapter(mAdapter);
        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet()) {
            noConnectionTextView.setVisibility(View.INVISIBLE);
            mProgressbarLoading.setVisibility(View.VISIBLE);
            new GetMoviesList().execute(NetworkUtils.buildMovieUrl(getString(R.string.popular)));
        }else{
            mProgressbarLoading.setVisibility(View.INVISIBLE);
            noConnectionTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
           preparePopularMovies(item);
            return true;
        } else if (id == R.id.action_top_rated) {
            prepareTopMovies(item);
            return true;
        }else if (id == R.id.action_favourite){
           prepareFavouriteMovies(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        IntentTools.goToMovieDetailsFromItem(this, movieList.get(clickedItemIndex).getId());
    }

    private void preparePopularMovies(MenuItem item){
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
        mProgressbarLoading.setVisibility(View.VISIBLE);
        mMoviesList.setVisibility(View.GONE);
        setTitle(R.string.popular_title);
        if (connectionDetector.isConnectingToInternet()) {
            mProgressbarLoading.setVisibility(View.VISIBLE);
            noConnectionTextView.setVisibility(View.INVISIBLE);
            new GetMoviesList().execute(NetworkUtils.buildMovieUrl(getString(R.string.popular)));
        }else{
            mProgressbarLoading.setVisibility(View.INVISIBLE);
            noConnectionTextView.setVisibility(View.VISIBLE);
        }
    }

    private void prepareTopMovies(MenuItem item){
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
        mProgressbarLoading.setVisibility(View.VISIBLE);
        mMoviesList.setVisibility(View.GONE);
        setTitle(R.string.top_rated_title);
        if (connectionDetector.isConnectingToInternet()) {
            mProgressbarLoading.setVisibility(View.VISIBLE);
            noConnectionTextView.setVisibility(View.INVISIBLE);
            new GetMoviesList().execute(NetworkUtils.buildMovieUrl(getString(R.string.top_rated)));
        }else{
            mProgressbarLoading.setVisibility(View.INVISIBLE);
            noConnectionTextView.setVisibility(View.VISIBLE);
        }
    }

    private void prepareFavouriteMovies(MenuItem item){
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
        setTitle(R.string.favourite_title);
        ArrayList<Movie> list= new ArrayList<>();
        mAdapter.setList(list);
    }


    class GetMoviesList extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            URL movieUrl = params[0];
            String moviesJson = null;
            ArrayList<Movie> list = null;
            try {
                moviesJson = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                list = NetworkUtils.getArrayFromJson(moviesJson, getString(R.string.movie_results));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> list) {
            mProgressbarLoading.setVisibility(View.GONE);

            if (list != null && list.size() != 0) {
                movieList = list;
                mMoviesList.setVisibility(View.VISIBLE);
                 mAdapter.setList(list);
            }
        }
    }



}
