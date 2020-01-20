package com.rohitshinde.app.moviesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitshinde.app.moviesapp.R;
import com.rohitshinde.app.moviesapp.bean.MovieBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MovieBean> movieBeanList;

    private OnItemClickListener mOnItemClickListener;

    public MovieGridAdapter(List<MovieBean> movieBeanList) {
        this.movieBeanList = movieBeanList;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid_image, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            final String imagePath = movieBeanList.get(position).getMoviePoster();
            Picasso.get()
                    .load(imagePath)
                    .error(R.drawable.image_not_available)
                    .into(view.image);

            view.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(movieBeanList.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movieBeanList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(MovieBean obj);
    }

    private class OriginalViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;

        private OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
        }
    }

}