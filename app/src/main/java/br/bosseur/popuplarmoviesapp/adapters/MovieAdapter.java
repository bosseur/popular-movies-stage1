package br.bosseur.popuplarmoviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.bosseur.popuplarmoviesapp.R;
import br.bosseur.popuplarmoviesapp.model.Movie;
import br.bosseur.popuplarmoviesapp.utilities.NetworkUtils;

/**
 * Adapter for showing movies.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> mMovieData;

    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(MovieAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    /**
     * The interface that defines methods for handling clicks on @Movie onClick messages.
     */
    public interface MovieAdapterOnClickHandler {

        /**
         * Handles a simple click on a @Movie
         *
         * @param clickedMovie The movie that was clicked
         */
        void onClick(Movie clickedMovie);
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.movie_item, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    /**
     * Bind the view to the Movie
     *
     * @param holder   The ViewHolder that sould be updated with the content of the new item
     * @param position The position if the item to be shown
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie selectedMovie = mMovieData.get(position);
        Context context = holder.itemView.getContext();
        // TODO change the way to get the width of the image
        String urlImage = NetworkUtils.buildImageUrl(selectedMovie.getPosterPath().substring(1), "w185").toString();
        Log.d(TAG, selectedMovie.toString());
        Picasso.with(context).load(urlImage).into(holder.mMoviePosterImageView);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items movies in the list
     */
    @Override
    public int getItemCount() {
        if (mMovieData == null) {
            return 0;
        }
        return mMovieData.size();
    }

    /**
     * Set the movie list for the adapter and notify the adapter that the dataset has changed
     *
     * @param mMovieData The list of movies that should be shown.
     */
    public void setMovieData(List<Movie> mMovieData) {
        this.mMovieData = mMovieData;
        notifyDataSetChanged();
    }

    /**
     * The Viewholder for the @{@link MovieAdapter}  for caching the children views
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected final ImageView mMoviePosterImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_list_picture);
            itemView.setOnClickListener(this);
        }

        /**
         * Implemented method from the @{@link View.OnClickListener}
         * @param view
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie selectedMovie = mMovieData.get(position);
            mClickHandler.onClick(selectedMovie);
        }
    }
}
