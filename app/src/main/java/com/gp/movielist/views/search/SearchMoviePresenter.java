package com.gp.movielist.views.search;

import com.gp.movielist.api.service.MovieService;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/** Created by gilbert on 12/13/18. */
public class SearchMoviePresenter implements SearchMovieContract.Presenter {

  private final CompositeDisposable disposable;
  private final MovieService movieService;
  private final SearchMovieContract.View view;

  private final AtomicInteger atomicInteger = new AtomicInteger(1);
  private final AtomicReference<String> oldQuery = new AtomicReference<>();

  public SearchMoviePresenter(MovieService movieService, SearchMovieContract.View view) {
    this.disposable = new CompositeDisposable();
    this.movieService = movieService;
    this.view = view;
  }

  @Override
  public void performReQuery() {
    searchMovie(oldQuery.get(), false);
  }

  @Override
  public void searchMovie(String query, boolean reset) {
    oldQuery.set(query);
    if (reset) atomicInteger.set(1);
    disposable.add(
        movieService
            .searchMovies(query, atomicInteger.get())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                movieContainer -> {
                  view.onSearchMovieResults(movieContainer);
                  atomicInteger.incrementAndGet();
                },
                t -> view.onSearchMovieFailed(t, reset)));
  }

  @Override
  public void onStart() {}

  @Override
  public void onStop() {
    disposable.clear();
  }
}
