package com.gp.movielist.api.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gp.movielist.api.typeadapters.DateTypeAdapter;

import org.threeten.bp.LocalDate;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/** Created by gilbert on 12/13/18. */
public class ApiUtils {
  private static volatile OkHttpClient client;
  private static volatile Gson gson;

  private static Object LOCK = new Object();

  public static void initOkHttpClient(String apiKey, boolean debug) {
    if (client == null) {
      synchronized (LOCK) {
        OkHttpClient.Builder builder =
            new OkHttpClient.Builder().addInterceptor(new ApiInjector(apiKey));

        if (debug) {
          HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
          loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
          builder.addInterceptor(loggingInterceptor);
        }

        client = builder.build();
      }
    } else {
      throw new IllegalStateException("OkHttpClient already initialized!");
    }
  }

  public static OkHttpClient getClient() {
    if (client == null) throw new IllegalStateException("Call ApiUtils.init() first!");

    return client;
  }

  public static Gson getGsonInstance() {
    if (gson == null) {
      synchronized (LOCK) {
        if (gson == null) {
          gson =
              new GsonBuilder()
                  .registerTypeAdapter(LocalDate.class, new DateTypeAdapter())
                  .create();
        }
      }
    }
    return gson;
  }
}
