package com.gp.movielist.views.search;

import com.gp.movielist.api.model.Container;
import com.gp.movielist.api.model.Movie;
import com.gp.movielist.base.BaseContract;

/**
 * Created by gilbert on 12/13/18.
 */
interface SearchMovieContract {

    interface View extends BaseContract.View<Presenter> {
        void onSearchMovieResults(Container<Movie> container);

        void onSearchMovieFailed(Throwable t, boolean resetPage);
    }

    interface Presenter extends BaseContract.Presenter {
        void searchMovie(String query, boolean reset);

        void performReQuery();
    }
}
