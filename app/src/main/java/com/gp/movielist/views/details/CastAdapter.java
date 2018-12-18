package com.gp.movielist.views.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.movielist.R;
import com.gp.movielist.api.model.Cast;
import com.gp.movielist.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/** Created by gilbert on 12/18/18. */
class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<Cast> casts;
  private List<Cast> raw;

  private static final int ITEM = 0;
  private static final int LOAD_MORE = 1;

  private boolean showAllCasts = false;

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    switch (viewType) {
      case ITEM:
        View itemView = inflater.inflate(R.layout.item_cast, parent, false);
        return new ItemHolder(itemView);
      default:
        View loadMoreHolder = inflater.inflate(R.layout.ite_view_load_more, parent, false);
        return new LoadMoreHolder(loadMoreHolder);
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ItemHolder) {
      ((ItemHolder) holder).bind(casts.get(position));
    }
  }

  public void setCasts(List<Cast> casts) {
    this.casts = new ArrayList<>();
    this.raw = new ArrayList<>();

    if (raw.size() == 5) {
      this.casts.addAll(casts);
    } else {
      this.raw.addAll(casts);

      int i = 0;
      while (i < 5) {
        this.casts.add(this.raw.get(i));
        i++;
      }
    }

    this.notifyDataSetChanged();
  }

  @Override
  public int getItemViewType(int position) {
    if (!showAllCasts && position == 4) {
      return LOAD_MORE;
    }
    return ITEM;
  }

  @Override
  public int getItemCount() {
    return casts == null ? 0 : casts.size();
  }

  class ItemHolder extends RecyclerView.ViewHolder {

    final TextView tvCastName;
    final TextView tvCastRole;
    final ImageView ivCast;

    ItemHolder(View itemView) {
      super(itemView);

      tvCastName = itemView.findViewById(R.id.tvCastName);
      tvCastRole = itemView.findViewById(R.id.tvCastRole);
      ivCast = itemView.findViewById(R.id.ivCast);
    }

    void bind(Cast cast) {
      tvCastName.setText(cast.getName());
      tvCastRole.setText(
          tvCastRole
              .getContext()
              .getResources()
              .getString(R.string.lbl_character, cast.getCharacter()));

      Picasso.get()
          .load(cast.getProfilePath())
          .transform(new CircleTransform())
          .placeholder(R.drawable.vec_person_place_holder)
          .error(R.drawable.vec_person_place_holder)
          .fit()
          .centerCrop()
          .into(ivCast);
    }
  }

  class LoadMoreHolder extends RecyclerView.ViewHolder {

    final Button btnLoadMore;

    LoadMoreHolder(View itemView) {
      super(itemView);

      btnLoadMore = ((Button) itemView);

      btnLoadMore.setText(R.string.btn_see_all_casts);
      btnLoadMore.setOnClickListener(
          v -> {
            showAllCasts = true;
            int oldSize = casts.size();
            casts.addAll(raw);
            notifyItemRangeInserted(oldSize, casts.size());
          });
    }
  }
}
