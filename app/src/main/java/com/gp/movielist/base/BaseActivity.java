package com.gp.movielist.base;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/** Created by gilbert on 12/13/18. */
public abstract class BaseActivity<T extends BaseContract.Presenter> extends AppCompatActivity
    implements BaseContract.View<T> {
  protected T presenter;

  @Override
  public void setPresenter(T presenter) {
    this.presenter = presenter;
  }

  @Override
  public void handleError(Throwable t) {
    Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
  }

  private static final BaseContract.Presenter BLANK_PRESENTER =
      new BaseContract.Presenter() {
        @Override
        public void onStart() {}

        @Override
        public void onStop() {}
      };

  @Override
  protected void onStart() {
    super.onStart();

    if (presenter == null) {
      presenter = (T) BLANK_PRESENTER;
      presenter.onStart();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.onStop();
  }
}
