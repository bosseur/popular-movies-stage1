package br.bosseur.popuplarmoviesapp.listeners;

import com.android.volley.Response;

import org.json.JSONException;

import java.util.List;

import br.bosseur.popuplarmoviesapp.utilities.MovieAppConverterUtil;


public class MovieApiResponseListener<T> implements Response.Listener<String> {

  private TaskListener listener;
  private Class<T> clazz;

  public MovieApiResponseListener(TaskListener listener, Class<T> clazz) {
    this.listener = listener;
    this.clazz = clazz;
  }

  @Override
  public void onResponse(String response) {
    try {
      List<T> resultList = MovieAppConverterUtil.buildResultList(response, clazz);
      listener.onCompleteTask(resultList, clazz);
    } catch (JSONException e) {
      listener.onError(e.getMessage());
    }

  }
}
