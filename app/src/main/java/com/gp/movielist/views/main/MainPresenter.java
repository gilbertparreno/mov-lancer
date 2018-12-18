package com.gp.movielist.views.main;

import com.gp.movielist.api.model.Container;
import com.gp.movielist.api.model.Movie;
import com.gp.movielist.api.service.MovieService;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/** Created by gilbert on 12/13/18. */
public class MainPresenter implements MainContract.Presenter {

  private static final int POPULAR = 0;
  private static final int TOP_RATED = 1;
  private static final int UPCOMING = 2;

  private final CompositeDisposable disposable;
  private final MovieService movieService;
  private final MainContract.View view;

  private final AtomicInteger atomicInteger = new AtomicInteger(1);

  public MainPresenter(MovieService movieService, MainContract.View view) {
    this.disposable = new CompositeDisposable();
    this.movieService = movieService;
    this.view = view;
  }

  @Override
  public void getMovies(int category, boolean resetPage) {
    if (resetPage) atomicInteger.set(1);
    disposable.add(
        getCategory(category)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                container -> {
                  view.onMovieSuccess(container);
                  atomicInteger.incrementAndGet();
                },
                t -> view.onMovieFailed(t, resetPage)));
  }

  @Override
  public void onStart() {}

  @Override
  public void onStop() {
    disposable.clear();
  }

  private Observable<Container<Movie>> getCategory(int category) {
    switch (category) {
      case POPULAR:
        return movieService.getPopularMovies(atomicInteger.get());
      case TOP_RATED:
        return movieService.getTopRatedMovies(atomicInteger.get());
      default:
        return movieService.getUpcomingMovies(atomicInteger.get());
    }
  }
}
