package com.gp.movielist.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.gp.movielist.api.utils.Constants;

/** Created by gilbert on 12/18/18. */
public class Cast implements Parcelable {
  @SerializedName("id")
  private int id;

  @SerializedName("cast_id")
  private String castId;

  @SerializedName("character")
  private String character;

  @SerializedName("name")
  private String name;

  @SerializedName("profile_path")
  private String profilePath;

  public int getId() {
    return id;
  }

  public String getCastId() {
    return castId;
  }

  public String getCharacter() {
    return character;
  }

  public String getName() {
    return name;
  }

  public String getProfilePath() {
    return Constants.PREFIX_IMAGE_URL + profilePath;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.castId);
    dest.writeString(this.character);
    dest.writeString(this.name);
    dest.writeString(this.profilePath);
  }

  public Cast() {}

  protected Cast(Parcel in) {
    this.id = in.readInt();
    this.castId = in.readString();
    this.character = in.readString();
    this.name = in.readString();
    this.profilePath = in.readString();
  }

  public static final Parcelable.Creator<Cast> CREATOR =
      new Parcelable.Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel source) {
          return new Cast(source);
        }

        @Override
        public Cast[] newArray(int size) {
          return new Cast[size];
        }
      };
}
