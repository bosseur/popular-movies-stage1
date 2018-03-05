package br.bosseur.popuplarmoviesapp.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import br.bosseur.popuplarmoviesapp.R;
import br.bosseur.popuplarmoviesapp.listeners.TaskListener;
import br.bosseur.popuplarmoviesapp.model.Movie;
import br.bosseur.popuplarmoviesapp.utilities.MovieAppConverterUtil;
import br.bosseur.popuplarmoviesapp.utilities.NetworkUtils;

/**
 * Created by EKoetsier on 29/01/2018.
 */

public class MovieListTask extends AsyncTask<String, Void, List<Movie>> {

  private static final String TAG = MovieListTask.class.getSimpleName();

  private String errorMessage;
  private Context context;
  private TaskListener listener;

  public MovieListTask(Context context, TaskListener listener) {
    this.context = context;
    this.listener = listener;
  }

  private boolean isOnline() {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    return cm.getActiveNetworkInfo() != null &&
        cm.getActiveNetworkInfo().isConnectedOrConnecting();
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    errorMessage = "";
    listener.onStartTask();

  }

  @Override
  protected List<Movie> doInBackground(String... urls) {
    if (urls == null || urls.length == 0 || !isOnline()) {
      if (!isOnline()) {
        errorMessage = context.getString(R.string.no_internet_message);
      }
      return null;
    }

    URL urlMovieList = NetworkUtils.buildMovieUrl(urls[0]);
    try {
      String response = NetworkUtils.getResponseFromHttpUrl(urlMovieList);
      return MovieAppConverterUtil.buildResultList(response, Movie.class);
    } catch (IOException e) {
      Log.d(TAG, e.getMessage());
      errorMessage = context.getString(R.string.standard_error_message, e.getMessage());
    } catch (JSONException e) {
      Log.d(TAG, e.getMessage());
      errorMessage = context.getString(R.string.standard_error_message, e.getMessage());
    }
    return null;
  }

  @Override
  protected void onPostExecute(List<Movie> movieList) {

    if (movieList != null) {
      listener.onCompleteTask(movieList, Movie.class);
    } else {
      listener.onError(errorMessage);
    }
  }
}
