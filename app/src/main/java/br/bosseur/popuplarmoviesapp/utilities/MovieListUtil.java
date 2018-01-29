package br.bosseur.popuplarmoviesapp.utilities;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.bosseur.popuplarmoviesapp.model.Movie;

/**
 * Utility class that works with {@link Movie}
 */

public class MovieListUtil {

    private static final String TAG = MovieListUtil.class.getSimpleName();

    private static final String resultKey = "results";

    /**
     * Transforms the json to a list of movies.
     *
     * @param jsonDataMovies Data received from the movie api
     * @return
     */
    public static List<Movie> buildList(String jsonDataMovies) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonDataMovies);
        JSONArray moviesArray = jsonObject.getJSONArray(resultKey);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        for (int i = 0; i < moviesArray.length(); i++) {
            Movie movie = gson.fromJson(moviesArray.getString(i), Movie.class);
            Log.d(TAG, movie.toString());
            movies.add(movie);
        }

        return movies;

    }
}
