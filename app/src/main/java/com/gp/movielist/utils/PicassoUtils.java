package com.gp.movielist.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import static android.content.Context.ACTIVITY_SERVICE;

/** Created by gilbert on 12/16/18. */
public class PicassoUtils {

  public static Picasso getCustomPicasso(Context context) {
    Picasso.Builder builder = new Picasso.Builder(context);
    builder.memoryCache(new LruCache(getBytesForMemCache(context)));
    builder.listener((picasso, uri, exception) -> Log.d("image load error", uri.getPath()));

    return builder.build();
  }

  private static int getBytesForMemCache(Context context) {
    ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
    ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
    activityManager.getMemoryInfo(mi);

    double availableMemory = mi.availMem;

    return (int) (12 * availableMemory / 100);
  }
}
