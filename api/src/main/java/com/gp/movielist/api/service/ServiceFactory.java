package com.gp.movielist.api.service;

import com.google.gson.Gson;
import com.gp.movielist.api.utils.ApiUtils;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/** Created by gilbert on 12/13/18. */
public class ServiceFactory {

  private static Retrofit retrofit;

  public static void init(String url, boolean debug, String apiKey) {
    ApiUtils.initOkHttpClient(apiKey, debug);
    Gson gson = ApiUtils.getGsonInstance();
    retrofit =
        new Retrofit.Builder()
            .baseUrl(url)
            .client(ApiUtils.getClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
  }

  public static <T> T getService(Class<T> clazz) {
    if (retrofit == null) throw new IllegalStateException("Call ServiceFactory.init() first!");
    return retrofit.create(clazz);
  }
}
