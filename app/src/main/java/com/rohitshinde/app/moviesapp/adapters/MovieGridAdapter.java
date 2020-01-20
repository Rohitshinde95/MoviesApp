package com.rohitshinde.app.moviesapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.rohitshinde.app.moviesapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> images = new ArrayList<>();
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public MovieGridAdapter(Context context, List<String> images) {
        this.images = images;
        this.context = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid_image, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            String imagePath=images.get(position);
            Log.e("TAG", "Loaded Path=" + imagePath);
            Picasso.get()
                    .load(imagePath)
                    .error(R.mipmap.ic_no_image_available)
                    .into(view.image, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError(Exception e) {
                    Log.e("","Error");
                }
            });

            view.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

}