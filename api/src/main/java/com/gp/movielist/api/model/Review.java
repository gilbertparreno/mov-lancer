package com.gp.movielist.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/** Created by gilbert on 12/17/18. */
public class Review implements Parcelable {
  @SerializedName("author")
  private String author;

  @SerializedName("content")
  private String content;

  @SerializedName("id")
  private String id;

  @SerializedName("url")
  private String url;

  public String getAuthor() {
    return author;
  }

  public String getContent() {
    return content;
  }

  public String getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.author);
    dest.writeString(this.content);
    dest.writeString(this.id);
    dest.writeString(this.url);
  }

  public Review() {}

  protected Review(Parcel in) {
    this.author = in.readString();
    this.content = in.readString();
    this.id = in.readString();
    this.url = in.readString();
  }

  public static final Parcelable.Creator<Review> CREATOR =
      new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
          return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
          return new Review[size];
        }
      };
}
