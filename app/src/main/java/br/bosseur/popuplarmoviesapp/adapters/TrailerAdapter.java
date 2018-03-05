package br.bosseur.popuplarmoviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.bosseur.popuplarmoviesapp.R;
import br.bosseur.popuplarmoviesapp.listeners.AdapterOnClickHandler;
import br.bosseur.popuplarmoviesapp.model.Trailer;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

  private static final String TAG = MovieAdapter.class.getSimpleName();

  private AdapterOnClickHandler<Trailer> mClickHandler;
  private List<Trailer> mTrailerData;

  public TrailerAdapter(AdapterOnClickHandler<Trailer> mClicHanlder) {
    this.mClickHandler = mClicHanlder;
  }

  @Override
  public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.trailer_list_item, parent, false);
    return new TrailerAdapter.TrailerAdapterViewHolder(view);
  }

  @Override
  public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
    Trailer trailer = mTrailerData.get(position);
    holder.traileTitleTextView.setText(trailer.getName());
  }


  @Override
  public int getItemCount() {
    if (mTrailerData == null) {
      return 0;
    }
    return mTrailerData.size();
  }

  /**
   * Set the list of trailers for the adapter and notify the adapter that the dataset has changed
   *
   * @param mTrailerData The list of movies that should be shown.
   */
  public void setTrailerData(List<Trailer> mTrailerData) {
    this.mTrailerData = mTrailerData;
    notifyDataSetChanged();
  }

  public List<Trailer> getTrailerData() {
    return mTrailerData;
  }

  /**
   * The Viewholder for the @{@link MovieAdapter}  for caching the children views
   */
  public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected final TextView traileTitleTextView;

    public TrailerAdapterViewHolder(View itemView) {
      super(itemView);
      traileTitleTextView = (TextView) itemView.findViewById(R.id.tv_trailer_title);
      itemView.setOnClickListener(this);
    }

    /**
     * Implemented method from the @{@link View.OnClickListener}
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
      int position = getAdapterPosition();
      Trailer selectedTrailer = mTrailerData.get(position);
      mClickHandler.onClick(selectedTrailer);
    }
  }
}
