package com.gp.movielist.views.main;

import com.gp.movielist.api.model.Container;
import com.gp.movielist.api.model.Movie;
import com.gp.movielist.base.BaseContract;

/** Created by gilbert on 12/13/18. */
interface MainContract {

  interface View extends BaseContract.View<Presenter> {
    void onMovieSuccess(Container<Movie> container);

    void onMovieFailed(Throwable t, boolean resetPage);
  }

  interface Presenter extends BaseContract.Presenter {
    void getMovies(int category, boolean resetPage);
  }
}
