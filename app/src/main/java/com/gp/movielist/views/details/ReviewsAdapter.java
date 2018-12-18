package com.gp.movielist.views.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.gp.movielist.R;
import com.gp.movielist.api.model.Review;
import com.gp.movielist.utils.ExpandableTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/** Created by gilbert on 12/17/18. */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

  private List<Review> reviews;

  private final ColorGenerator colorGenerator;
  private final TextDrawable.IBuilder builder;

  public ReviewsAdapter() {
    colorGenerator = ColorGenerator.MATERIAL;
    builder = TextDrawable.builder().beginConfig().endConfig().round();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_review, parent, false);
    return new ViewHolder(view);
  }

  public void setReviews(List<Review> reviews) {
    if (this.reviews == null || this.reviews == Collections.EMPTY_LIST) {
      this.reviews = new ArrayList<>();
    }

    this.reviews.addAll(reviews);
    this.notifyDataSetChanged();
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bind(reviews.get(position));
  }

  @Override
  public int getItemCount() {
    return reviews == null ? 0 : reviews.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    final TextView tvAuthor;
    final ExpandableTextView tvReviewContent;
    final ImageView ivAuthor;

    ViewHolder(View itemView) {
      super(itemView);
      tvAuthor = itemView.findViewById(R.id.tvAuthor);
      tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
      ivAuthor = itemView.findViewById(R.id.ivAuthor);
    }

    void bind(Review review) {
      int color = colorGenerator.getColor(review.getId());
      TextDrawable textDrawable =
          builder.build(review.getAuthor().substring(0, 1).toUpperCase(), color);
      tvAuthor.setText(review.getAuthor());
      tvReviewContent.setText(review.getContent());
      ivAuthor.setImageDrawable(textDrawable);
    }
  }
}
