package com.gp.movielist.views.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.gp.movielist.R;
import com.gp.movielist.api.model.Container;
import com.gp.movielist.api.model.Movie;
import com.gp.movielist.api.service.MovieService;
import com.gp.movielist.api.service.ServiceFactory;
import com.gp.movielist.base.BaseActivity;
import com.gp.movielist.utils.MovieSwipeRefreshLayout;
import com.gp.movielist.views.details.MovieDetailsActivity;
import com.gp.movielist.views.search.SearchMovieActivity;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityOptionsCompat;

public class MainActivity extends BaseActivity<MainContract.Presenter>
    implements MainContract.View {

  private MovieSwipeRefreshLayout slMovies;
  private AppCompatSpinner spnCategories;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    slMovies = findViewById(R.id.slMovies);
    spnCategories = findViewById(R.id.spnCategories);
    ArrayAdapter<CharSequence> adapter =
        ArrayAdapter.createFromResource(this, R.array.arr_categories, R.layout.spinner_item);
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
    spnCategories.setAdapter(adapter);
    spnCategories.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            slMovies.resetAdapter();
            presenter.getMovies(position, true);
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });

    findViewById(R.id.ivSearchMovie)
        .setOnClickListener(v -> startActivity(new Intent(this, SearchMovieActivity.class)));

    presenter = new MainPresenter(ServiceFactory.getService(MovieService.class), this);
    slMovies.setRefreshing(true);
    slMovies
        .getAdapter()
        .setListener(
            new MovieAdapter.AdapterEventListener() {
              @Override
              public void onEndReached() {
                if (presenter != null)
                  presenter.getMovies(spnCategories.getSelectedItemPosition(), false);
              }

              @Override
              public void onClickRefresh() {
                if (presenter != null) {
                  presenter.getMovies(spnCategories.getSelectedItemPosition(), false);
                }
              }

              @Override
              public void onMovieClick(Movie movie, ImageView ivMovie) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE, movie);
                ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this, ivMovie, ivMovie.getTransitionName());
                startActivity(intent, options.toBundle());
              }
            });

    slMovies.setOnRefreshListener(
        () -> presenter.getMovies(spnCategories.getSelectedItemPosition(), true));
  }

  @Override
  public void onMovieSuccess(Container container) {
    if (slMovies.isRefreshing()) {
      slMovies.post(() -> slMovies.setRefreshing(false));
    }
    slMovies.addData(container);
  }

  @Override
  public void onMovieFailed(Throwable t, boolean resetPage) {
    if (slMovies.isRefreshing()) {
      slMovies.post(() -> slMovies.setRefreshing(false));
    }
    if (resetPage) slMovies.setCurrentPage(0);
    slMovies.showError(t.getMessage());
  }
}
