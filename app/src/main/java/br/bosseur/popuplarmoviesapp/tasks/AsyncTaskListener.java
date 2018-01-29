package br.bosseur.popuplarmoviesapp.tasks;

/**
 * Created by EKoetsier on 29/01/2018.
 */

public interface AsyncTaskListener<T> {
    void onStartTask();
    void onError(String errorMessage);
    void onCompleteTask(T data);
}
