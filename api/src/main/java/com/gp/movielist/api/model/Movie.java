package com.gp.movielist.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gp.movielist.api.utils.Constants;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/** Created by gilbert on 12/13/18. */
public class Movie implements Parcelable {
  @SerializedName("id")
  private int id;

  @SerializedName("vote_count")
  private int videoCount;

  @SerializedName("video")
  private boolean isVideo;

  @SerializedName("vote_average")
  private float voteAverage;

  @SerializedName("title")
  private String title;

  @SerializedName("poster_path")
  private String posterPath;

  @SerializedName("backdrop_path")
  private String backdropPath;

  @SerializedName("release_date")
  private LocalDate releaseDate;

  @SerializedName("overview")
  private String overview;

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({Status.IN_PROGRESS, Status.ERROR})
  public @interface Status {
    int SUCCESS = 0;
    int IN_PROGRESS = 1;
    int ERROR = 2;
    int END_RESULT = 3;
  }

  @Expose(deserialize = false, serialize = false)
  private int status = Status.SUCCESS;

  public Movie() {}

  public Movie(@Status int status) {
    this.status = status;
  }

  public int getId() {
    return id;
  }

  public int getVideoCount() {
    return videoCount;
  }

  public boolean isVideo() {
    return isVideo;
  }

  public float getVoteAverage() {
    return voteAverage;
  }

  public float getRating() {
    return Math.round(voteAverage / 2);
  }

  public String getTitle() {
    return title;
  }

  public String getPosterPath() {
    return Constants.PREFIX_IMAGE_URL + posterPath;
  }

  public String getBackdropPath() {
    return Constants.PREFIX_IMAGE_URL + backdropPath;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public String getFormattedReleaseDate() {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    return releaseDate.format(dateTimeFormatter);
  }

  public String getOverview() {
    return overview;
  }

  @Status
  public int getStatus() {
    return status;
  }

  public void setStatus(@Status int status) {
    this.status = status;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeInt(this.videoCount);
    dest.writeByte(this.isVideo ? (byte) 1 : (byte) 0);
    dest.writeFloat(this.voteAverage);
    dest.writeString(this.title);
    dest.writeString(this.posterPath);
    dest.writeString(this.backdropPath);
    dest.writeSerializable(this.releaseDate);
    dest.writeString(this.overview);
    dest.writeInt(this.status);
  }

  protected Movie(Parcel in) {
    this.id = in.readInt();
    this.videoCount = in.readInt();
    this.isVideo = in.readByte() != 0;
    this.voteAverage = in.readFloat();
    this.title = in.readString();
    this.posterPath = in.readString();
    this.backdropPath = in.readString();
    this.releaseDate = (LocalDate) in.readSerializable();
    this.overview = in.readString();
    this.status = in.readInt();
  }

  public static final Parcelable.Creator<Movie> CREATOR =
      new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
          return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
          return new Movie[size];
        }
      };
}
