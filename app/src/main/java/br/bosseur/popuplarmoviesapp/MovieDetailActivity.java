package br.bosseur.popuplarmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.bosseur.popuplarmoviesapp.model.Movie;
import br.bosseur.popuplarmoviesapp.utilities.NetworkUtils;

import static br.bosseur.popuplarmoviesapp.MoviesActivity.MOVIE_TAG;

/**
 * Activity for showing details about a @{@link Movie}
 */
public class MovieDetailActivity extends AppCompatActivity {

    private ImageView backdropImage;
    private ImageView posterImageView;
    private TextView synopsisTextView;
    private TextView ratingTextView;
    private TextView releaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        backdropImage = (ImageView) findViewById(R.id.backdrop_image_view);
        posterImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        synopsisTextView = (TextView) findViewById(R.id.tv_movie_plot);
        ratingTextView = (TextView) findViewById(R.id.tv_movie_rating);
        releaseDateTextView = (TextView) findViewById(R.id.tv_release_date);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MOVIE_TAG)) {
            Movie selectedMovie = intent.getParcelableExtra(MOVIE_TAG);
            collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.background_light));
            collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(android.R.color.background_light));
            collapsingToolbar.setTitle(selectedMovie.getTitle());

            String urlImage = NetworkUtils.buildImageUrl(selectedMovie.getPosterPath().substring(1), "w185").toString();
            Picasso.with(this).load(urlImage).into(posterImageView);

            urlImage = NetworkUtils.buildImageUrl(selectedMovie.getBackdropPath().substring(1), "w185").toString();
            Picasso.with(this).load(urlImage).into(backdropImage);

            synopsisTextView.setText(selectedMovie.getOverview());
            ratingTextView.setText(getString(R.string.movie_rating, selectedMovie.getVoteAverage()));
            releaseDateTextView.setText(getString(R.string.release_date, selectedMovie.getReleaseDate()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
