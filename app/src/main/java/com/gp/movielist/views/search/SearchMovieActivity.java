package com.gp.movielist.views.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.gp.movielist.R;
import com.gp.movielist.api.model.Container;
import com.gp.movielist.api.model.Movie;
import com.gp.movielist.api.service.MovieService;
import com.gp.movielist.api.service.ServiceFactory;
import com.gp.movielist.base.BaseActivity;
import com.gp.movielist.utils.MovieSwipeRefreshLayout;
import com.gp.movielist.views.details.MovieDetailsActivity;
import com.gp.movielist.views.main.MovieAdapter;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;

/** Created by gilbert on 12/17/18. */
public class SearchMovieActivity extends BaseActivity<SearchMovieContract.Presenter>
    implements SearchMovieContract.View {

  private static final int START_SEARCH = 0;
  private static final int PERFORM_RE_QUERY = 1;
  private static final int CANCEL_SEARCH = 2;

  private EditText inputSearch;
  private MovieSwipeRefreshLayout slMovies;

  private Handler handler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_movie);

    presenter = new SearchMoviePresenter(ServiceFactory.getService(MovieService.class), this);

    handler =
        new Handler(
            msg -> {
              switch (msg.what) {
                case START_SEARCH:
                  handler.removeMessages(START_SEARCH);
                  slMovies.setRefreshing(true);
                  presenter.searchMovie(msg.obj.toString(), true);
                  break;
                case PERFORM_RE_QUERY:
                  presenter.performReQuery();
                  break;
                case CANCEL_SEARCH:
                  slMovies.setRefreshing(false);
                  slMovies.getAdapter().resetList();
                  break;
              }

              return true;
            });

    inputSearch = findViewById(R.id.inputSearch);
    slMovies = findViewById(R.id.slMovies);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    slMovies
        .getAdapter()
        .setListener(
            new MovieAdapter.AdapterEventListener() {
              @Override
              public void onEndReached() {
                handler.sendMessage(handler.obtainMessage(PERFORM_RE_QUERY));
              }

              @Override
              public void onClickRefresh() {
                handler.sendMessage(handler.obtainMessage(PERFORM_RE_QUERY));
              }

              @Override
              public void onMovieClick(Movie movie, ImageView ivMovie) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE, movie);
                ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        SearchMovieActivity.this, ivMovie, ivMovie.getTransitionName());
                startActivity(intent, options.toBundle());
              }
            });

    inputSearch.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            handler.removeMessages(START_SEARCH);
            handler.sendMessage(handler.obtainMessage(CANCEL_SEARCH));
          }

          @Override
          public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s.toString())) {
              handler.sendMessageDelayed(handler.obtainMessage(START_SEARCH, s), 500);
            }
          }
        });

    slMovies.setOnRefreshListener(
        () -> {
          String query = inputSearch.getText().toString();
          if (TextUtils.isEmpty(query)) {
            slMovies.setRefreshing(false);
          } else {
            handler.sendMessage(handler.obtainMessage(START_SEARCH, query));
            slMovies.setRefreshing(true);
          }
        });
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public void onSearchMovieResults(Container<Movie> container) {
    if (slMovies.isRefreshing()) {
      slMovies.post(() -> slMovies.setRefreshing(false));
    }
    slMovies.addData(container);
  }

  @Override
  public void onSearchMovieFailed(Throwable t, boolean resetPage) {
    if (slMovies.isRefreshing()) {
      slMovies.post(() -> slMovies.setRefreshing(false));
    }
    if (resetPage) slMovies.setCurrentPage(0);
    slMovies.showError(t.getMessage());
  }
}
