package com.gp.movielist.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/** Created by gilbert on 12/17/18. */
public class Videos implements Parcelable {
  @SerializedName("id")
  private String id;

  @SerializedName("key")
  private String key;

  @SerializedName("name")
  private String name;

  @SerializedName("site")
  private String site;

  public String getId() {
    return id;
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public String getSite() {
    return site;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.key);
    dest.writeString(this.name);
    dest.writeString(this.site);
  }

  public Videos() {}

  protected Videos(Parcel in) {
    this.id = in.readString();
    this.key = in.readString();
    this.name = in.readString();
    this.site = in.readString();
  }

  public static final Parcelable.Creator<Videos> CREATOR =
      new Parcelable.Creator<Videos>() {
        @Override
        public Videos createFromParcel(Parcel source) {
          return new Videos(source);
        }

        @Override
        public Videos[] newArray(int size) {
          return new Videos[size];
        }
      };
}
