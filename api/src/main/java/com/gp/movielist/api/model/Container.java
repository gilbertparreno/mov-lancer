package com.gp.movielist.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Created by gilbert on 12/13/18. */
public class Container<T> {
  @SerializedName("page")
  private int page;

  @SerializedName("total_pages")
  private int totalPages;

  @SerializedName("total_results")
  private int totalResults;

  @SerializedName(
      value = "results",
      alternate = {"cast"})
  private List<T> data;

  public int getPage() {
    return page;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public List<T> getData() {
    return data;
  }

  public int getTotalResults() {
    return totalResults;
  }
}
