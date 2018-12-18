package com.gp.movielist.api.model;

import java.util.List;

/** Created by gilbert on 12/13/18. */
public class Page<T> {
  private List<T> data;

  public List<T> getData() {
    return data;
  }
}
