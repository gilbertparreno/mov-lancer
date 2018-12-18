package com.gp.movielist;

import android.app.Application;

import com.gp.movielist.api.service.ServiceFactory;
import com.gp.movielist.utils.PicassoUtils;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.picasso.Picasso;

/** Created by gilbert on 12/13/18. */
public class App extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    AndroidThreeTen.init(this);
    Picasso.setSingletonInstance(PicassoUtils.getCustomPicasso(this));
    ServiceFactory.init(BuildConfig.BASE_URL, BuildConfig.DEBUG, BuildConfig.API_KEY);
  }
}
