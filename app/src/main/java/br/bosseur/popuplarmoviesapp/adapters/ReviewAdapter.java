package br.bosseur.popuplarmoviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.bosseur.popuplarmoviesapp.R;
import br.bosseur.popuplarmoviesapp.model.Review;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

  private static final String TAG = MovieAdapter.class.getSimpleName();

  private List<Review> mReviewData;

  public ReviewAdapter() {
  }

  @Override
  public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.review_list_item, parent, false);
    return new ReviewAdapter.ReviewAdapterViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
    Review review = mReviewData.get(position);
    holder.reviewTitleTextView.setText(review.getContent());
    holder.authorTextView.setText(review.getAuthor());
  }


  @Override
  public int getItemCount() {
    if (mReviewData == null) {
      return 0;
    }
    return mReviewData.size();
  }

  /**
   * Set the list of Reviews for the adapter and notify the adapter that the dataset has changed
   *
   * @param mReviewData The list of movies that should be shown.
   */
  public void setReviewData(List<Review> mReviewData) {
    this.mReviewData = mReviewData;
    notifyDataSetChanged();
  }

  public List<Review> getReviewData() {
    return mReviewData;
  }

  /**
   * The Viewholder for the @{@link MovieAdapter}  for caching the children views
   */
  public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

    protected final TextView authorTextView;
    protected final TextView reviewTitleTextView;

    public ReviewAdapterViewHolder(View itemView) {
      super(itemView);
      authorTextView = (TextView) itemView.findViewById(R.id.tv_author);
      reviewTitleTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
    }

  }
}
