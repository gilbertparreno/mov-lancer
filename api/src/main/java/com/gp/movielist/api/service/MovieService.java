package com.gp.movielist.api.service;

import com.gp.movielist.api.model.Cast;
import com.gp.movielist.api.model.Container;
import com.gp.movielist.api.model.Movie;
import com.gp.movielist.api.model.Review;
import com.gp.movielist.api.model.Videos;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/** Created by gilbert on 12/13/18. */
public interface MovieService {
  @GET("movie/popular?language=en-US")
  Observable<Container<Movie>> getPopularMovies(@Query("page") int page);

  @GET("movie/top_rated?language=en-US")
  Observable<Container<Movie>> getTopRatedMovies(@Query("page") int page);

  @GET("movie/upcoming?language=en-US")
  Observable<Container<Movie>> getUpcomingMovies(@Query("page") int page);

  @GET("movie/{movie_id}/videos")
  Observable<Container<Videos>> getTrailers(@Path("movie_id") int movieId);

  @GET("movie/{movie_id}/reviews")
  Observable<Container<Review>> getReviews(@Path("movie_id") int movieId);

  @GET("search/movie")
  Observable<Container<Movie>> searchMovies(@Query("query") String query, @Query("page") int page);

  @GET("movie/{movie_id}/credits")
  Observable<Container<Cast>> getCasts(@Path("movie_id") int movieId);
}
