package com.udacity.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.data.NetworkUtils;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieReview;
import com.udacity.popularmovies.model.MovieTrailer;
import com.udacity.popularmovies.utilities.ConnectionDetector;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MovieDetailsActivity extends AppCompatActivity
        implements TrailerAdapter.ListItemClickListener, ReviewsAdapter.ListItemClickListener {

    private ConnectionDetector connectionDetector;
    private int movieId;
    private ProgressBar mProgressbarLoading;
    private ImageView moviePosterImageView;
    private TextView movieTitleTextView, overviewTextView;
    private RatingBar voteAverageRatingBar;
    private TabLayout tableLayout;
    private RecyclerView mTrailerList, mReviewList;
    private ArrayList<MovieTrailer> trailerArrayList;
    private ArrayList<MovieReview> reviewArrayList;
    private TrailerAdapter mTrailerAdapter;
    private ReviewsAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (getIntent() != null) {
            String key = getString(R.string.key_item_clicked_index);
            if (getIntent().hasExtra(key)) {
                movieId = getIntent().getIntExtra(key, -1);
            }
        }

        mProgressbarLoading = findViewById(R.id.progress_bar_loading);
        moviePosterImageView = findViewById(R.id.iv_poster);
        movieTitleTextView = findViewById(R.id.tv_movie_title);
//        movieReleaseDateTextView = findViewById(R.id.tv_movie_release_date);
        voteAverageRatingBar = findViewById(R.id.vote_rating_bar);
        overviewTextView = findViewById(R.id.tv_overview);
        mTrailerList = findViewById(R.id.rv_trailers);
        LinearLayoutManager trailerLinearLayoutManager = new LinearLayoutManager(this);
        mTrailerList.setLayoutManager(trailerLinearLayoutManager);
        mTrailerList.setHasFixedSize(true);
        mReviewList = findViewById(R.id.rv_reviews);
        LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(this);
        mReviewList.setLayoutManager(reviewLinearLayoutManager);
        mReviewList.setHasFixedSize(true);
        prepareTabs();
        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet()) {
            mProgressbarLoading.setVisibility(View.VISIBLE);
            new GetMovie().execute(NetworkUtils.buildMovieDetailsUrl(movieId));
            new GetMovieTrailer().execute(NetworkUtils.buildMovieTrailerUrl(movieId));
            new GetMovieReviews().execute(NetworkUtils.buildMovieReviewUrl(movieId));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void populateUI(Movie movie) {
        String imageUrl = NetworkUtils.buildImageUrl(movie.getPoster_path(), NetworkUtils.IMAGE_SIZE_DETAILS);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder)
                .into(moviePosterImageView);
        String[] date = movie.getRelease_date().split("-");
        movieTitleTextView.setText(movie.getTitle() + " (" + date[0] + ")");
//        movieReleaseDateTextView.setText(movie.getRelease_date());
        voteAverageRatingBar.setRating((movie.getVote_average() * 5) / 10);
        overviewTextView.setText(movie.getOverview());
    }

    private void prepareTabs() {
        tableLayout = findViewById(R.id.tab_details);
        tableLayout.addTab(tableLayout.newTab().setText( R.string.trailer));
        tableLayout.addTab(tableLayout.newTab().setText( R.string.reviews));
    }

    private void populateTrailerUI(ArrayList<MovieTrailer> trailerList) {
        trailerArrayList = trailerList;
        mTrailerAdapter = new TrailerAdapter(trailerList, this);
        mTrailerList.setAdapter(mTrailerAdapter);
    }

    private void populateReviewUI(ArrayList<MovieReview> reviewList) {
        reviewArrayList = reviewList;
        mReviewAdapter = new ReviewsAdapter(reviewArrayList, this);
        mTrailerList.setAdapter(mReviewAdapter);
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="
                + trailerArrayList.get(clickedItemIndex).getKey()
                + "\"")));

    }

    class GetMovie extends AsyncTask<URL, Void, Movie> {

        @Override
        protected Movie doInBackground(URL... params) {
            URL movieUrl = params[0];
            String moviesJson;
            Movie movie = null;
            try {
                moviesJson = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                movie = NetworkUtils.getMovie(moviesJson);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            mProgressbarLoading.setVisibility(View.GONE);
            if (movie != null) {
                populateUI(movie);
            }
        }
    }

    class GetMovieTrailer extends AsyncTask<URL, Void, ArrayList<MovieTrailer>> {

        @Override
        protected ArrayList<MovieTrailer> doInBackground(URL... params) {
            URL movieUrl = params[0];
            String moviesJson;
            ArrayList<MovieTrailer> trailerList = null;
            try {
                moviesJson = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                trailerList = NetworkUtils.getMovieTrailer(moviesJson, getString(R.string.movie_results));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return trailerList;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieTrailer> trailer) {
            if (trailer != null) {
                populateTrailerUI(trailer);
            }
        }
    }

    class GetMovieReviews extends AsyncTask<URL, Void, ArrayList<MovieReview>> {

        @Override
        protected ArrayList<MovieReview> doInBackground(URL... params) {
            URL movieUrl = params[0];
            String moviesJson;
            ArrayList<MovieReview> reviewList = null;
            try {
                moviesJson = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                reviewList = NetworkUtils.getMovieReviews(moviesJson, getString(R.string.movie_results));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reviewList;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieReview> reviewList) {
            if (reviewList != null) {
                populateReviewUI(reviewList);
            }
        }
    }




}
