package br.bosseur.popuplarmoviesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import br.bosseur.popuplarmoviesapp.data.MovieContract;
import br.bosseur.popuplarmoviesapp.listeners.AdapterOnClickHandler;
import br.bosseur.popuplarmoviesapp.listeners.ErrorListener;
import br.bosseur.popuplarmoviesapp.listeners.MovieApiResponseListener;
import br.bosseur.popuplarmoviesapp.listeners.TaskListener;
import br.bosseur.popuplarmoviesapp.model.Movie;
import br.bosseur.popuplarmoviesapp.utilities.MovieAppConverterUtil;
import br.bosseur.popuplarmoviesapp.utilities.NetworkUtils;

/**
 * The main activity of the app. On opening will show a list of movies on the screen.
 */
public class MoviesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>,
        AdapterOnClickHandler<Movie> {

    private static final String TAG = MoviesActivity.class.getSimpleName();

    private static final int MOVIE_SEARCH_LOADER = 44;
    private static final String SORT_ORDER_KEY = "sort_order";
    public static final String MOVIE_TAG = "MOVIE_TAG";

    private SharedPreferences preferences;

    private TextView errorMessageTextView;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar mLoadingIndicator;
    private MovieApiResponseListener<Movie> responseListener;
    private ErrorListener<List<Movie>> errorListener;

    private String currentSortOrder;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.movie_list_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_progress_indicator);
        errorMessageTextView = (TextView) findViewById(R.id.tv_error_message);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(this);

        recyclerView.setAdapter(movieAdapter);

        queryMovies();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        currentSortOrder = preferences.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
    }

    /**
     * Saves the sort order in the bundle
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_ORDER_KEY, currentSortOrder);
    }

    /**
     * Create the menu for this Activity
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        if (getString(R.string.popular_movie_url).equals(currentSortOrder)) {
            menu.findItem(R.id.popular).setChecked(true);
        } else if (getString(R.string.top_rated_movie_url).equals(currentSortOrder)) {
            menu.findItem(R.id.top_rated).setChecked(true);
        } else if (getString(R.string.pref_favorite_value).equals(currentSortOrder)) {
            menu.findItem(R.id.menu_favorites).setChecked(true);
        }

        return true;
    }

    /**
     * Handles clicks on items in the menu.
     *
     * @param item The {@link MenuItem}
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuSelected = item.getItemId();
        movieAdapter.setMovieData(Collections.<Movie>emptyList());
        item.setChecked(true);
        switch (menuSelected) {
            case R.id.popular:
                currentSortOrder = getString(R.string.popular_movie_url);
                break;
            case R.id.top_rated:
                currentSortOrder = getString(R.string.top_rated_movie_url);
                break;
            case R.id.menu_favorites:
                currentSortOrder = getString(R.string.pref_favorite_value);
            default:
        }

        updatePreference();
        queryMovies();
        return super.onOptionsItemSelected(item);
    }

    private void queryMovies() {
        String preferenceOrderKey = getString(R.string.pref_sort_order_key);
        String selectedOerder = preferences.getString(preferenceOrderKey, getString(R.string.pref_sort_order_default));
        Bundle order = new Bundle();

        order.putString(preferenceOrderKey, selectedOerder);

        android.support.v4.app.LoaderManager loaderManager  = getSupportLoaderManager();
        android.support.v4.content.Loader<Object> loader = loaderManager.getLoader(MOVIE_SEARCH_LOADER);

        if(loader == null ) {
            loaderManager.initLoader(MOVIE_SEARCH_LOADER, order, this);
        } else {
            loaderManager.restartLoader(MOVIE_SEARCH_LOADER, order, this);
        }
    }

    private void updatePreference() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.pref_sort_order_key), currentSortOrder);
        editor.apply();
    }

    private void showMovieLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showMovieView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
    }

    private void showError(String errorMessage) {
        errorMessageTextView.setText(errorMessage);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
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

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(bundle == null) return;
                recyclerView.setVisibility(View.INVISIBLE);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public List<Movie> loadInBackground() {
                String order = bundle.getString(getString(R.string.pref_sort_order_key));

                if(order.equals(getString(R.string.pref_favorite_value))){
                    Uri favoriteUri = MovieContract.MovieEntry.CONTENT_URI;

                    Cursor cursor = MoviesActivity.this.getContentResolver().query(favoriteUri,
                            null,
                            null,
                            null,
                            null);

                    return MovieAppConverterUtil.buildListFromCursor(cursor);

                }else {
                    URL urlMovieList = NetworkUtils.buildMovieUrl(order);
                    try {
                        String response = NetworkUtils.getResponseFromHttpUrl(urlMovieList);
                        return MovieAppConverterUtil.buildResultList(response, Movie.class);
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage());
                    } catch (JSONException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movieList) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movieList != null && movieList.size() > 0) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            showMovieView();
            movieAdapter.setMovieData(movieList);
        } else {
            showError(getString(R.string.error_no_movie_found));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        movieAdapter.setMovieData(Collections.<Movie>emptyList());
    }
}
