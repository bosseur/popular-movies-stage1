package br.bosseur.popuplarmoviesapp.utilities;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.bosseur.popuplarmoviesapp.data.MovieContract;
import br.bosseur.popuplarmoviesapp.model.Movie;

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

  public static ContentValues buildContentValues(Movie movie) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
    contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
    contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
    contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
    contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
    contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
    contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
    contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
    contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
    contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
    return contentValues;
  }

  public static Movie buildFromCursor(Cursor cursor) {

    if (cursor != null
        && !cursor.isClosed()
        && !cursor.isAfterLast()
        && !cursor.isBeforeFirst()) {

      Movie movie = new Movie();

      movie.setId(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
      movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH)));
      movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE)));
      movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
      movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
      movie.setPopularity(cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY)));
      movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
      movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
      movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
      movie.setVoteAverage(cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
      movie.setVoteCount(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT)));

      return movie;
    }

    return null;

  }

  public static List<Movie> buildListFromCursor(Cursor cursor) {
    List<Movie> movies = new ArrayList<>();
    if (cursor != null
        && !cursor.isClosed()) {
      while (cursor.moveToNext()) {
        Movie movie = buildFromCursor(cursor);
        movies.add(movie);
      }
    }

    return movies;
  }
}
