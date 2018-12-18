package com.gp.movielist.api.typeadapters;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;

/** Created by gilbert on 12/17/18. */
public class DateTypeAdapter extends TypeAdapter<LocalDate> {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Override
  public void write(JsonWriter out, LocalDate value) throws IOException {
    if (value == null) {
      out.nullValue();
    } else {
      out.value(value.toString());
    }
  }

  @Override
  public LocalDate read(JsonReader in) throws IOException {
    String dateString = in.nextString();
    if (TextUtils.isEmpty(dateString)) {
      return null;
    } else {
      LocalDate localDate = LocalDate.parse(dateString, DATE_TIME_FORMATTER);
      return localDate;
    }
  }
}
