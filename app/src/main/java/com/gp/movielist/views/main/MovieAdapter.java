package com.gp.movielist.views.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gp.movielist.R;
import com.gp.movielist.api.model.Movie;
import com.gp.movielist.utils.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/** Created by gilbert on 12/13/18. */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public interface AdapterEventListener {
    void onEndReached();

    void onClickRefresh();

    void onMovieClick(Movie movie, ImageView ivMovie);
  }

  private List<Movie> movies;
  private AdapterEventListener listener;

  public void addMovies(List<Movie> movies, int page) {
    int page1 = page;
    if (this.movies == null || page1 == 1) this.movies = new ArrayList<>();
    else {
      // last movie object that hold progress = true that needs to be remove
      int index = this.movies.size() - 1;
      this.movies.remove(index);
      notifyItemRemoved(index);
    }

    int oldSize = this.movies.size();
    this.movies.addAll(movies);
    this.movies.add(updateStatus(Movie.Status.IN_PROGRESS));
    notifyItemRangeChanged(oldSize, this.movies.size() - 1);
  }

  public void resetList() {
    if (this.movies != null) {
      this.movies.clear();
      notifyDataSetChanged();
    }
  }

  public Movie updateStatus(@Movie.Status int status) {
    int lastIndex = movies.size() - 1;
    Movie m = movies.get(lastIndex);
    if (m.getStatus() == Movie.Status.SUCCESS) {
      Movie movie = new Movie(status);
      notifyItemChanged(lastIndex);
      return movie;
    } else {
      m.setStatus(status);
      movies.set(lastIndex, m);
      notifyItemChanged(lastIndex);
      return m;
    }
  }

  public void onError() {
    if (movies == null || movies == Collections.EMPTY_LIST) return;

    updateStatus(Movie.Status.ERROR);
  }

  public void setListener(AdapterEventListener listener) {
    this.listener = listener;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());

    switch (viewType) {
      case Movie.Status.SUCCESS:
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(movieView);
      case Movie.Status.IN_PROGRESS:
        View pbView = inflater.inflate(R.layout.item_movie_pb, parent, false);
        return new ProgressViewHolder(pbView);
      case Movie.Status.ERROR:
      case Movie.Status.END_RESULT:
        View statusView = inflater.inflate(R.layout.item_movie_status, parent, false);
        return new StatusViewHolder(statusView);
      default:
        throw new IllegalArgumentException("Invalid Movie status!");
    }
  }

  @Override
  public int getItemViewType(int position) {
    return movies.get(position).getStatus();
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    Movie movie = movies.get(position);
    switch (movie.getStatus()) {
      case Movie.Status.SUCCESS:
        ((MovieViewHolder) holder).bind(movie);
        break;
      case Movie.Status.END_RESULT:
      case Movie.Status.ERROR:
        ((StatusViewHolder) holder).bind(movie);
        break;
      case Movie.Status.IN_PROGRESS:
        if (listener != null) listener.onEndReached();
        break;
    }
  }

  @Override
  public int getItemCount() {
    return (movies == null ? 0 : movies.size());
  }

  class MovieViewHolder extends RecyclerView.ViewHolder {
    final ImageView ivMovie;
    final RatingBar rbMovie;
    final TextView tvMovieName;

    MovieViewHolder(View itemView) {
      super(itemView);
      ivMovie = itemView.findViewById(R.id.ivMovie);
      rbMovie = itemView.findViewById(R.id.rbMovie);
      tvMovieName = itemView.findViewById(R.id.tvMovieName);
    }

    void bind(Movie m) {
      tvMovieName.setText(m.getTitle());
      rbMovie.setRating(m.getRating());
      Picasso.get()
          .load(m.getPosterPath())
          .placeholder(R.drawable.vec_placeholder_movie)
          .error(R.drawable.vec_placeholder_movie)
          .fit()
          .transform(
              new RoundedCornersTransformation(15, 8, RoundedCornersTransformation.CornerType.ALL))
          .into(ivMovie);

      itemView.setOnClickListener(
          v -> {
            if (listener != null) {
              listener.onMovieClick(m, ivMovie);
            }
          });
    }
  }

  class StatusViewHolder extends RecyclerView.ViewHolder {
    final TextView tvError;
    final View ivStatus;

    StatusViewHolder(View itemView) {
      super(itemView);
      tvError = itemView.findViewById(R.id.tvError);
      ivStatus = itemView.findViewById(R.id.ivStatus);
    }

    void bind(Movie m) {
      if (m.getStatus() == Movie.Status.END_RESULT) {
        tvError.setText(R.string.lbl_max_result);
        ivStatus.setVisibility(View.GONE);
        itemView.setOnClickListener(null);
        return;
      }
      itemView.setOnClickListener(
          v -> {
            if (listener != null) {
              updateStatus(Movie.Status.IN_PROGRESS);
              listener.onClickRefresh();
            }
          });
    }
  }

  class ProgressViewHolder extends RecyclerView.ViewHolder {
    ProgressViewHolder(View itemView) {
      super(itemView);
    }
  }
}
