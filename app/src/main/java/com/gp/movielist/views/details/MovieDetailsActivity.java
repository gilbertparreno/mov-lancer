package com.gp.movielist.views.details;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gp.movielist.R;
import com.gp.movielist.api.model.Cast;
import com.gp.movielist.api.model.Container;
import com.gp.movielist.api.model.Movie;
import com.gp.movielist.api.model.Review;
import com.gp.movielist.api.model.Videos;
import com.gp.movielist.api.service.MovieService;
import com.gp.movielist.api.service.ServiceFactory;
import com.gp.movielist.base.BaseActivity;
import com.gp.movielist.utils.EmptyRecyclerView;
import com.gp.movielist.utils.RoundedCornersTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MovieDetailsActivity extends BaseActivity<MovieDetailsContract.Presenter>
    implements MovieDetailsContract.View, TrailerAdapter.ItemClickListener {

  public static final String EXTRA_MOVIE = "movie";
  private static final String YOUTUBE_LINK = "https://www.youtube.com/watch?v=";
  private static final String YOUTUBE_APP = "vnd.youtube://";

  private Movie movie;
  private EmptyRecyclerView emptyReviews;
  private EmptyRecyclerView emptyTrailers;
  private TextView tvReviews;
  private TrailerAdapter trailerAdapter;
  private ReviewsAdapter reviewsAdapter;
  private CastAdapter castAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    presenter = new MovieDetailsPresenter(ServiceFactory.getService(MovieService.class), this);

    setContentView(R.layout.activity_movie_details);

    Bundle bundle = getIntent().getExtras();
    if (bundle.containsKey(EXTRA_MOVIE)) {
      movie = bundle.getParcelable(EXTRA_MOVIE);
    } else {
      throw new IllegalArgumentException("Please pass Movie object using Intent.putExtra().");
    }

    initMovieDetails();
    initTrailer();
    initReviews();
    initCast();
  }

  private void initCast() {
    RecyclerView rvCasts = findViewById(R.id.rvCasts);
    castAdapter = new CastAdapter();

    rvCasts.setAdapter(castAdapter);
    rvCasts.setLayoutManager(new LinearLayoutManager(this));
  }

  @Override
  protected void onStart() {
    super.onStart();
    presenter.getMovieTrailers(movie.getId());
    presenter.getReviewsAndCasts(movie.getId());
  }

  private void initReviews() {
    tvReviews = findViewById(R.id.tvReviews);
    tvReviews.setText(getString(R.string.lbl_reviews, 0));

    emptyReviews = findViewById(R.id.emptyReviews);
    reviewsAdapter = new ReviewsAdapter();
    emptyReviews.setLayoutManager(new LinearLayoutManager(this));
    emptyReviews.getRecyclerView().setAdapter(reviewsAdapter);
  }

  private void initTrailer() {
    emptyTrailers = findViewById(R.id.emptyTrailers);
    trailerAdapter = new TrailerAdapter(this);
    emptyTrailers.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    emptyTrailers.getRecyclerView().setAdapter(trailerAdapter);
  }

  private void initMovieDetails() {
    ImageView ivMovie = findViewById(R.id.ivMovie);
    ImageView ivMovieBanner = findViewById(R.id.ivMovieBanner);
    ((RatingBar) findViewById(R.id.rbMovie)).setRating(movie.getRating());

    supportPostponeEnterTransition();
    
    Picasso.get()
        .load(movie.getPosterPath())
        .placeholder(R.drawable.vec_placeholder_movie)
        .error(R.drawable.vec_placeholder_movie)
        .networkPolicy(NetworkPolicy.OFFLINE)
        .transform(
            new RoundedCornersTransformation(15, 0, RoundedCornersTransformation.CornerType.ALL))
        .into(
            ivMovie,
            new Callback() {
              @Override
              public void onSuccess() {
                supportStartPostponedEnterTransition();
              }

              @Override
              public void onError(Exception e) {
                supportStartPostponedEnterTransition();
              }
            });

    Picasso.get()
        .load(movie.getBackdropPath())
        .fit()
        .placeholder(R.drawable.vec_placeholder_movie)
        .error(R.drawable.vec_placeholder_movie)
        .into(ivMovieBanner);

    ((TextView) findViewById(R.id.tvMovieReleaseDate)).setText(movie.getFormattedReleaseDate());
    ((TextView) findViewById(R.id.tvOverViewContent)).setText(movie.getOverview());
    ((TextView) findViewById(R.id.tvMovieName))
        .setText(
            getString(
                R.string.lbl_format_movie_title,
                movie.getTitle(),
                String.valueOf(movie.getReleaseDate().getYear())));
    findViewById(R.id.ivNavBack).setOnClickListener(v -> supportFinishAfterTransition());
  }

  @Override
  public void onMovieTrailerSuccess(Container<Videos> container) {
    if (container.getData().size() == 0) {
      emptyTrailers.showEmptyText(true);
    } else {
      emptyTrailers.showEmptyText(false);
      trailerAdapter.setVideos(container.getData());
    }
  }

  @Override
  public void onMovieTrailerFailed(Throwable t) {}

  @Override
  public void onGetReviewSuccess(Container<Review> container) {
    if (container.getData().size() == 0) {
      emptyReviews.showEmptyText(true);
    } else {
      emptyReviews.showEmptyText(false);
      tvReviews.setText(getString(R.string.lbl_reviews, container.getTotalResults()));
      reviewsAdapter.setReviews(container.getData());
    }
  }

  @Override
  public void onGetCastsSuccess(Container<Cast> container) {
    castAdapter.setCasts(container.getData());
  }

  @Override
  public void onItemClick(Videos videos) {
    try {
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP + videos.getKey()));
      startActivity(intent);
    } catch (ActivityNotFoundException e) {
      e.printStackTrace();
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_LINK + videos.getKey()));
      startActivity(intent);
    }
  }
}
