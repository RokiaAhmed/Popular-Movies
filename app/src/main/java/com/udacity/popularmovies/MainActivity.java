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

import com.udacity.popularmovies.data.NetworkUtils;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utilities.IntentTools;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    RecyclerView mMoviesList;
    MoviesAdapter mAdapter;
    ProgressBar mProgressbarLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressbarLoading = findViewById(R.id.progress_bar_loading);
        mMoviesList = findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mMoviesList.setLayoutManager(gridLayoutManager);
        mMoviesList.setHasFixedSize(true);
        mAdapter = new MoviesAdapter( (MoviesAdapter.ListItemClickListener)this);
        mMoviesList.setAdapter(mAdapter);

        mProgressbarLoading.setVisibility(View.VISIBLE);
        new GetMoviesList().execute(NetworkUtils.buildMovieUrl(getString(R.string.popular)));

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
            mProgressbarLoading.setVisibility(View.VISIBLE);
            mMoviesList.setVisibility(View.GONE);
            setTitle(R.string.popular_title);
            new GetMoviesList().execute(NetworkUtils.buildMovieUrl(getString(R.string.popular)));
            return true;
        } else if (id == R.id.action_top_rated) {
            mProgressbarLoading.setVisibility(View.VISIBLE);
            mMoviesList.setVisibility(View.GONE);
            setTitle(R.string.top_rated_title);
            new GetMoviesList().execute(NetworkUtils.buildMovieUrl(getString(R.string.top_rated)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        IntentTools.goToMovieDetailsFromItem(this, clickedItemIndex);
    }


    class GetMoviesList extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String moviesJson = null;
            ArrayList<Movie> moviesList = null;
            try {
                moviesJson = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                moviesList = NetworkUtils.getArrayFromJson(moviesJson, getString(R.string.movie_results));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return moviesList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieList) {
            mProgressbarLoading.setVisibility(View.GONE);

            if (movieList != null && movieList.size() != 0) {
                mMoviesList.setVisibility(View.VISIBLE);
                mAdapter.setList(movieList);
            }
        }
    }

}
