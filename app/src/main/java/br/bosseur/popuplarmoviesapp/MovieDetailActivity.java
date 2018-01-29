package br.bosseur.popuplarmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private TextView titleTextView;
    private ImageView posterImageView;
    private TextView synopsisTextView;
    private TextView ratingTextView;
    private TextView releaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        titleTextView = (TextView) findViewById(R.id.tv_movie_title);
        posterImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        synopsisTextView = (TextView) findViewById(R.id.tv_movie_plot);
        ratingTextView = (TextView) findViewById(R.id.tv_movie_rating);
        releaseDateTextView = (TextView) findViewById(R.id.tv_release_date);

        Intent intent = getIntent();
        Movie selectedMovie = intent.getParcelableExtra(MOVIE_TAG);
        if (selectedMovie != null) {
            titleTextView.setText(selectedMovie.getTitle());
            String urlImage = NetworkUtils.buildImageUrl(selectedMovie.getPosterPath().substring(1), "w185").toString();
            Picasso.with(this).load(urlImage).into(posterImageView);

            synopsisTextView.setText(selectedMovie.getOverview());
            ratingTextView.setText(getString(R.string.movie_rating, selectedMovie.getVoteAverage()));
            releaseDateTextView.setText(getString(R.string.release_date, selectedMovie.getReleaseDate()));
        }
    }
}
