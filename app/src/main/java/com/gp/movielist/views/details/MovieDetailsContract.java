package com.gp.movielist.views.details;

import com.gp.movielist.api.model.Cast;
import com.gp.movielist.api.model.Container;
import com.gp.movielist.api.model.Review;
import com.gp.movielist.api.model.Videos;
import com.gp.movielist.base.BaseContract;

/** Created by gilbert on 12/13/18. */
interface MovieDetailsContract {

  interface View extends BaseContract.View<Presenter> {
    void onMovieTrailerSuccess(Container<Videos> container);

    void onMovieTrailerFailed(Throwable t);

    void onGetReviewSuccess(Container<Review> container);

    void onGetCastsSuccess(Container<Cast> container);
  }

  interface Presenter extends BaseContract.Presenter {
    void getMovieTrailers(int movieId);

    void getReviewsAndCasts(int movieId);
  }
}
