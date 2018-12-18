package com.gp.movielist.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.gp.movielist.R;
import com.gp.movielist.api.model.Container;
import com.gp.movielist.api.model.Movie;
import com.gp.movielist.views.main.MovieAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/** Created by gilbert on 12/13/18. */
public class MovieSwipeRefreshLayout extends SwipeRefreshLayout {

  private final TextView tvEmpty;
    private final MovieAdapter adapter = new MovieAdapter();
  private int currentPage = 0;
  private final View llMovieContainer;

  public MovieSwipeRefreshLayout(Context context) {
    this(context, null);
  }

  public MovieSwipeRefreshLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    inflate(getContext(), R.layout.view_empty_swiperefresh_layout, this);

      RecyclerView rvMovies = findViewById(R.id.rvMovies);
    tvEmpty = findViewById(R.id.tvEmpty);
    llMovieContainer = findViewById(R.id.rlMovieContainer);

    rvMovies = findViewById(R.id.rvMovies);
    rvMovies.setLayoutManager(new GridLayoutManager(getContext(), 2));
    rvMovies.setAdapter(adapter);

    ((GridLayoutManager) rvMovies.getLayoutManager())
        .setSpanSizeLookup(
            new GridLayoutManager.SpanSizeLookup() {
              @Override
              public int getSpanSize(int position) {
                if (adapter.getItemCount() - 1 == position) {
                  return 2;
                }
                return 1;
              }
            });
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public MovieAdapter getAdapter() {
    return adapter;
  }

  public void addData(@NonNull Container container) {
    if (container.getData() == null || container.getData().size() == 0) {
      tvEmpty.setVisibility(VISIBLE);
      llMovieContainer.setVisibility(GONE);
      return;
    }

    tvEmpty.setVisibility(GONE);
    llMovieContainer.setVisibility(VISIBLE);

    adapter.addMovies(container.getData(), container.getPage());
    currentPage = container.getPage();
    if (container.getPage() == container.getTotalPages()) {
      adapter.updateStatus(Movie.Status.END_RESULT);
    }
  }

  public void resetAdapter() {
    adapter.resetList();
  }

  public void showError(String error) {
    if (currentPage == 0) {
      tvEmpty.setVisibility(VISIBLE);
      tvEmpty.setText(error);
      llMovieContainer.setVisibility(GONE);
    } else {
      adapter.onError();
    }
  }
}
