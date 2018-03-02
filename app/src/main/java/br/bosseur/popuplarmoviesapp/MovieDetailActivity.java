package br.bosseur.popuplarmoviesapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import br.bosseur.popuplarmoviesapp.adapters.ReviewAdapter;
import br.bosseur.popuplarmoviesapp.adapters.TrailerAdapter;
import br.bosseur.popuplarmoviesapp.data.MovieContract;
import br.bosseur.popuplarmoviesapp.listeners.AdapterOnClickHandler;
import br.bosseur.popuplarmoviesapp.listeners.ErrorListener;
import br.bosseur.popuplarmoviesapp.listeners.MovieApiResponseListener;
import br.bosseur.popuplarmoviesapp.listeners.TaskListener;
import br.bosseur.popuplarmoviesapp.model.Movie;
import br.bosseur.popuplarmoviesapp.model.Review;
import br.bosseur.popuplarmoviesapp.model.Trailer;
import br.bosseur.popuplarmoviesapp.utilities.NetworkUtils;

import static br.bosseur.popuplarmoviesapp.MoviesActivity.MOVIE_TAG;

/**
 * Activity for showing details about a @{@link Movie}
 */
public class MovieDetailActivity extends AppCompatActivity implements
        AdapterOnClickHandler<Trailer>,
        TaskListener {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private Toast mToast;
    private Movie selectedMovie;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private ImageButton mFavoriteImageButton;
    private ImageView backdropImage;
    private ImageView posterImageView;
    private TextView synopsisTextView;
    private TextView ratingTextView;
    private TextView releaseDateTextView;

    private RecyclerView mReviewRecyclerView;
    private RecyclerView mTrailerRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mErrorMessgae;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        mFavoriteImageButton = (ImageButton) findViewById(R.id.iv_favorite);
        backdropImage = (ImageView) findViewById(R.id.backdrop_image_view);
        posterImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        synopsisTextView = (TextView) findViewById(R.id.tv_movie_plot);
        ratingTextView = (TextView) findViewById(R.id.tv_movie_rating);
        releaseDateTextView = (TextView) findViewById(R.id.tv_release_date);


        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress_indicator);
        mErrorMessgae = (TextView) findViewById(R.id.tv_error_message);


        LinearLayoutManager layoutManagerTrailer = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailer_list);
        mTrailerRecyclerView.setLayoutManager(layoutManagerTrailer);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        LinearLayoutManager layoutManagerReview = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rv_review_list);
        mReviewRecyclerView.setLayoutManager(layoutManagerReview);
        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MOVIE_TAG)) {
            selectedMovie = intent.getParcelableExtra(MOVIE_TAG);
            collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.background_light));
            collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(android.R.color.background_light));
            collapsingToolbar.setTitle(selectedMovie.getTitle());

            String urlImage = NetworkUtils.buildImageUrl(selectedMovie.getPosterPath().substring(1), "w185").toString();
            Picasso.with(this)
                    .load(urlImage)
                    .into(posterImageView);

            urlImage = NetworkUtils.buildImageUrl(selectedMovie.getBackdropPath().substring(1), "w185").toString();
            Picasso.with(this)
                    .load(urlImage)
                    .into(backdropImage);

            synopsisTextView.setText(selectedMovie.getOverview());
            NumberFormat nf = new DecimalFormat("0.0");

            String votingFormat = nf.format(selectedMovie.getVoteAverage());
            ratingTextView.setText(getString(R.string.voting_label, votingFormat));
            releaseDateTextView.setText(selectedMovie.getReleaseDate().split("-")[0]);

            URL trailerUrl = NetworkUtils.buildTrailerUrl(String.valueOf(selectedMovie.getId()));
            query(trailerUrl, Trailer.class);

            URL reviewUrl = NetworkUtils.buildReviewUrl(String.valueOf(selectedMovie.getId()));
            query(reviewUrl, Review.class);
        }

    }

    private void query(URL queryUrl, Class clazz) {
        String url = queryUrl.toString();
        onStartTask();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new MovieApiResponseListener<>(this, clazz), new ErrorListener<>(this));
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onStartTask() {
        mTrailerRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessgae.setVisibility(View.GONE);
    }

    @Override
    public void onError(String errorMessage) {
        mProgressBar.setVisibility(View.GONE);
        mErrorMessgae.setVisibility(View.VISIBLE);
        mErrorMessgae.setError(errorMessage);
    }

    @Override
    public void onCompleteTask(List data, Class clazz) {
        mProgressBar.setVisibility(View.GONE);
        if (Trailer.class.equals(clazz)) {
            mTrailerRecyclerView.setVisibility(View.VISIBLE);
            mTrailerAdapter.setTrailerData(data);
        } else if (Review.class.equals(clazz)) {
            mReviewRecyclerView.setVisibility(View.VISIBLE);
            mReviewAdapter.setReviewData(data);
        }
    }

    // Code taken from https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
    @Override
    public void onClick(Trailer item) {
        String id = item.getKey();
        Uri appUri = Uri.parse("vnd.youtube:" + id);
        Log.d(TAG, "Uri app:" + appUri);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, appUri);

        PackageManager packageManager = getPackageManager();

        if (appIntent.resolveActivity(packageManager) != null)
            startActivity(appIntent);
        else {
            Uri webUri = Uri.parse("http://www.youtube.com/watch?v=" + id);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
            startActivity(webIntent);
        }
    }

    public void changeFavorite(View view) {
        ContentResolver resolver = getContentResolver();
        int favoriteIndicatorId;
        String message;
        if (this.selectedMovie.isFavorite()) {
            Uri deleteUri = MovieContract.MovieEntry.CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(selectedMovie.getId()))
                    .build();
            int numRemoved = resolver.delete(deleteUri, null, null);
            favoriteIndicatorId = android.R.drawable.star_off;
            message = getString(R.string.movie_favorite_removed_alert);
        } else {
            Uri insertUri = MovieContract.MovieEntry.CONTENT_URI;
            ContentValues contentValues = buildContentValues();
            resolver.insert(insertUri, contentValues);
            favoriteIndicatorId = android.R.drawable.star_on;
            message = getString(R.string.movie_favorite_added_alert);
        }

        this.selectedMovie.setFavorite(!this.selectedMovie.isFavorite());
        mFavoriteImageButton.setImageResource(favoriteIndicatorId);
        if( mToast != null ){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();

    }

    @NonNull
    private ContentValues buildContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, selectedMovie.getBackdropPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, selectedMovie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, selectedMovie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, selectedMovie.getOriginalLanguage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, selectedMovie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, selectedMovie.getPopularity());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, selectedMovie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, selectedMovie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, selectedMovie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, selectedMovie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, selectedMovie.getVoteCount());
        return contentValues;
    }
}
