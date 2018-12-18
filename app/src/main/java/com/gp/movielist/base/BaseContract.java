package com.gp.movielist.base;

/** Created by gilbert on 12/13/18. */
public interface BaseContract {

  interface View<T extends Presenter> {
    void setPresenter(T presenter);

    void handleError(Throwable t);
  }

  interface Presenter {
    void onStart();

    void onStop();
  }
}
