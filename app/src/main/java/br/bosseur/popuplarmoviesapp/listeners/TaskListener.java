package br.bosseur.popuplarmoviesapp.listeners;

import java.util.List;

/**
 * Interface for doing callbacks in staged calls
 * on internet connection
 */

public interface TaskListener {

    /**
     * Should be called when an internet call starts
     */
    void onStartTask();

    /**
     * Should be called when an error occurs.
     * @param errorMessage Explanation of the error that occurred.
     */
    void onError(String errorMessage);

    /**
     * Should be called when the call to the internet returns
     * passing the data that was received.
     * @param data The data that was returned by the call
     */
    void onCompleteTask(List<?> data, Class clazz);
}
