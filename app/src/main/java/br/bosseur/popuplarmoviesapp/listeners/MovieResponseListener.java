package br.bosseur.popuplarmoviesapp.listeners;

import com.android.volley.Response;

import org.json.JSONException;

import java.util.List;

import br.bosseur.popuplarmoviesapp.model.Movie;
import br.bosseur.popuplarmoviesapp.utilities.MovieListUtil;

/**
 *
 *
 *
 */

public class MovieResponseListener implements Response.Listener<String> {

    private TaskListener<List<Movie>> listener;

    public MovieResponseListener(TaskListener<List<Movie>> listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(String response) {

        try {
            List<Movie> movieList = MovieListUtil.buildList(response);
            listener.onCompleteTask(movieList);
        } catch (JSONException e) {
            listener.onError(e.getMessage());
        }

    }
}
