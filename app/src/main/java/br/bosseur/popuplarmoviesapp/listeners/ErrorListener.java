package br.bosseur.popuplarmoviesapp.listeners;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import br.bosseur.popuplarmoviesapp.model.Movie;

/**
 * Listener for handling errors;
 */

public class ErrorListener<T> implements Response.ErrorListener {

    private TaskListener listener;

    public ErrorListener(TaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        listener.onError(error.getMessage());
    }
}
