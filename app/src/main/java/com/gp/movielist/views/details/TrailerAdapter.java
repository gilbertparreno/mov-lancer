package com.gp.movielist.views.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gp.movielist.R;
import com.gp.movielist.api.model.Videos;
import com.gp.movielist.api.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/** Created by gilbert on 12/17/18. */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

  public interface ItemClickListener {
    void onItemClick(Videos videos);
  }

  private List<Videos> videos;
  private final ItemClickListener itemClickListener;

  public TrailerAdapter(ItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_trailer, parent, false);
    return new ViewHolder(view);
  }

  public void setVideos(List<Videos> videos) {
    if (this.videos == null || this.videos == Collections.EMPTY_LIST) {
      this.videos = new ArrayList<>();
    }

    this.videos.addAll(videos);
    notifyDataSetChanged();
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bind(videos.get(position));
  }

  @Override
  public int getItemCount() {
    return videos == null ? 0 : videos.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    final ImageView ivTrailer;

    ViewHolder(View itemView) {
      super(itemView);
      ivTrailer = itemView.findViewById(R.id.ivTrailer);
    }

    void bind(Videos videos) {
      String thumbnailTemplate = Constants.YOUTUBE_THUMBNAIL_URL;
      String path = thumbnailTemplate.replace(Constants.PATH_MOVIE_ID, videos.getKey());
      Picasso.get().load(path).into(ivTrailer);

      itemView.setOnClickListener(
          v -> {
            if (itemClickListener != null) {
              itemClickListener.onItemClick(videos);
            }
          });
    }
  }
}
