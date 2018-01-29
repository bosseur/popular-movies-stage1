package br.bosseur.popuplarmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import br.bosseur.popuplarmoviesapp.adapters.MovieAdapter;
import br.bosseur.popuplarmoviesapp.model.Movie;
import br.bosseur.popuplarmoviesapp.utilities.MovieListUtil;
import br.bosseur.popuplarmoviesapp.utilities.NetworkUtils;

/**
 * The main activity of the app. On opening will show a list of movies on the screen.
 */
public class MoviesActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MoviesActivity.class.getSimpleName();

    private static final String SORT_ORDER_KEY = "sort_order";
    public static final String MOVIE_TAG = "MOVIE_TAG";

    private TextView errorMessageTextView;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar mLoadingIndicator;

    private String sortOrder;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        retrieveSortOrder(savedInstanceState);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_progress_indicator);
        errorMessageTextView = (TextView) findViewById(R.id.tv_error_message);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(this);

        recyclerView.setAdapter(movieAdapter);

        queryMovies();

    }

    private void queryMovies() {
        new MovieListTask().execute(sortOrder);
    }

    private void retrieveSortOrder(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getString(SORT_ORDER_KEY) != null) {
            sortOrder = savedInstanceState.getString(SORT_ORDER_KEY);
        } else {
            sortOrder = getString(R.string.top_rated_movie_url);
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        retrieveSortOrder(savedInstanceState);
        queryMovies();
    }

    /**
     * Saves the sort order in the bundle
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_ORDER_KEY, sortOrder);
    }

    /**
     * Create the menu for this Activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handles clicks on items in the menu.
     * @param item The {@link MenuItem}
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuSelected = item.getItemId();

        switch (menuSelected) {
            case R.id.popular:
                sortOrder = getString(R.string.popular_movie_url);
                break;
            case R.id.top_rated:
                sortOrder = getString(R.string.top_rated_movie_url);
                break;
            default:
        }

        movieAdapter.setMovieData(Collections.EMPTY_LIST);
        queryMovies();
        return super.onOptionsItemSelected(item);
    }

    public class MovieListTask extends AsyncTask<String, Void, List<Movie>> {
        private String errorMessage;

        private boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null &&
                    cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            errorMessage = "";
            showMovieView();
            recyclerView.setVisibility(View.INVISIBLE);
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... urls) {
            if (urls == null || urls.length == 0 || !isOnline()) {
                if(!isOnline()){
                    errorMessage = getString(R.string.no_internet_message);
                }
                return null;
            }

            URL urlMovieList = NetworkUtils.buildMovieUrl(urls[0]);
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(urlMovieList);
                return MovieListUtil.buildList(response);
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                errorMessage = getString(R.string.standard_error_message, e.getMessage());
            } catch (JSONException e) {
                Log.d(TAG, e.getMessage());
                errorMessage = getString(R.string.standard_error_message, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (movieList != null) {
                showMovieView();
                movieAdapter.setMovieData(movieList);
            } else {
                showError(errorMessage);
            }
        }
    }

    private void showMovieView() {
        recyclerView.setVisibility(View.VISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
    }

    private void showError(String errorMessage) {
        errorMessageTextView.setText(errorMessage);
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Handles clicks on a {@link Movie} in the list.
     *
     * @param clickedMovie The movie that the user clicked on
     */
    @Override
    public void onClick(Movie clickedMovie) {
        Context context = this;
        Class activityClass = MovieDetailActivity.class;
        Intent intentToOpen = new Intent(context, activityClass);
        intentToOpen.putExtra(MOVIE_TAG, clickedMovie);
        startActivity(intentToOpen);
    }
}
