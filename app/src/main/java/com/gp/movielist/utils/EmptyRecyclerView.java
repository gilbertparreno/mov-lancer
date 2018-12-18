package com.gp.movielist.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gp.movielist.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/** Created by gilbert on 12/17/18. */
public class EmptyRecyclerView extends FrameLayout {

  private final RecyclerView recyclerView;
  private final TextView tvEmpty;

  public EmptyRecyclerView(Context context) {
    this(context, null);
  }

  public EmptyRecyclerView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    inflate(context, R.layout.view_empty_recyclerview, this);
    recyclerView = findViewById(R.id.recyclerView);
    tvEmpty = findViewById(R.id.tvEmpty);

    TypedArray a =
        context.getTheme().obtainStyledAttributes(attrs, R.styleable.EmptyRecyclerView, 0, 0);
    try {
      String emptyText = a.getString(R.styleable.EmptyRecyclerView_emptyText);
      if (!TextUtils.isEmpty(emptyText)) {
        tvEmpty.setText(emptyText);
      }
    } finally {
      a.recycle();
    }
  }

  public RecyclerView getRecyclerView() {
    return recyclerView;
  }

  public void setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {
    recyclerView.setLayoutManager(layoutManager);
  }

  public void setEmptyText(@NonNull String emptyText) {
    tvEmpty.setText(emptyText);
  }

  public void showEmptyText(boolean show) {
    tvEmpty.setVisibility(show ? VISIBLE : GONE);
    recyclerView.setVisibility(show ? GONE : VISIBLE);
  }
}
