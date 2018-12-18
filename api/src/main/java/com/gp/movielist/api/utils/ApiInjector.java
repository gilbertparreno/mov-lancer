package com.gp.movielist.api.utils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/** Created by gilbert on 12/13/18. */
public class ApiInjector implements Interceptor {

  private static final String API_KEY = "api_key";
  private static final String APPLICATION_JSON = "application/json";
  private static final String ACCEPT = "Accept";

  private String apiKey;

  public ApiInjector(String apiKey) {
    this.apiKey = apiKey;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Request.Builder builder = request.newBuilder();

    HttpUrl requestUrl = request.url().newBuilder().addQueryParameter(API_KEY, apiKey).build();

    request = builder.addHeader(ACCEPT, APPLICATION_JSON).url(requestUrl).build();
    return chain.proceed(request);
  }
}
