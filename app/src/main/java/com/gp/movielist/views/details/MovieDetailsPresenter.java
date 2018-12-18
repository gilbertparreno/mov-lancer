package com.gp.movielist.views.details;

import android.util.Pair;

import com.gp.movielist.api.service.MovieService;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/** Created by gilbert on 12/13/18. */
public class MovieDetailsPresenter implements MovieDetailsContract.Presenter {

  private static final int POPULAR = 0;
  private static final int TOP_RATED = 1;
  private static final int UPCOMING = 2;

  private final CompositeDisposable disposable;
  private final MovieService movieService;
  private final MovieDetailsContract.View view;

  private AtomicInteger atomicInteger = new AtomicInteger(1);

  public MovieDetailsPresenter(MovieService movieService, MovieDetailsContract.View view) {
    this.disposable = new CompositeDisposable();
    this.movieService = movieService;
    this.view = view;
  }

  @Override
  public void getMovieTrailers(int movieId) {
    disposable.add(
        movieService
            .getTrailers(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::onMovieTrailerSuccess, view::onMovieTrailerFailed));
  }

  @Override
  public void getReviewsAndCasts(int movieId) {
    disposable.add(
        Observable.zip(
                movieService.getReviews(movieId), movieService.getCasts(movieId), Pair::create)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                pair -> {
                  view.onGetReviewSuccess(pair.first);
                  view.onGetCastsSuccess(pair.second);
                },
                view::onMovieTrailerFailed));
  }

  @Override
  public void onStart() {}

  @Override
  public void onStop() {
    disposable.clear();
  }
}
