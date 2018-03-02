package br.bosseur.popuplarmoviesapp.listeners;

/**
 * Created by EKoetsier on 28/02/2018.
 */

public interface AdapterOnClickHandler<T> {

    /**
     * Handles a simple click on a Item in an @{@link android.support.v7.widget.RecyclerView.Adapter}
     *
     * @param item The item that was clicked
     */
    void onClick(T item);
}
