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
import br.bosseur.popuplarmoviesapp.model.Trailer;

/**
 * Utility class that works with {@link Movie}
 */

public class MovieAppConverterUtil {

    private static final String TAG = MovieAppConverterUtil.class.getSimpleName();

    private static final String resultKey = "results";

    /**
     * Transforms the json to a list of movies.
     *
     * @param jsonData Data received from the movie api
     * @return
     */
    public static <T> List<T> buildResultList(String jsonData, Class<T> transFormClass) throws JSONException {
        List<T> items = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray jsonDataArray = jsonObject.getJSONArray(resultKey);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        for (int i = 0; i < jsonDataArray.length(); i++) {
            T item = gson.fromJson(jsonDataArray.getString(i), transFormClass);
            Log.d(TAG, item.toString());
            items.add(item);
        }

        return items;

    }
}
