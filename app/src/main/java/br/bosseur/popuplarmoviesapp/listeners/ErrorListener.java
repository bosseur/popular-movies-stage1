package br.bosseur.popuplarmoviesapp.listeners;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import br.bosseur.popuplarmoviesapp.model.Movie;

/**
 * Listener for handling errors;
 */

public class ErrorListener implements Response.ErrorListener {

    private TaskListener<List<Movie>> listener;

    public ErrorListener(TaskListener<List<Movie>> listener) {
        this.listener = listener;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        listener.onError(error.getMessage());
    }
}
