package com.example.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.api.responses.ImagesResponse;
import com.example.mygallery.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public  class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {


    private  List<ImagesResponse> imagesResponses;
    private final ClickPhotoCallback callback;

    public PhotoAdapter( ClickPhotoCallback callback) {
        this.imagesResponses = new ArrayList<>();
        this.callback = callback;
    }

    public  List<ImagesResponse> getImagesResponses() {
        return imagesResponses;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyViewHolder.newInstance(parent, callback);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImagesResponse imagesResponse = imagesResponses.get(position);
        holder.bind(imagesResponse);
    }

    @Override
    public int getItemCount() {
        return imagesResponses.size();
    }

    public void addImages(List<ImagesResponse> images) {
        int size = imagesResponses.size();
        imagesResponses.addAll(images);
        if (size == 0) {
            notifyDataSetChanged();
        }
        else
        notifyItemRangeChanged(size, imagesResponses.size());
    }

    public void clearData() {
        imagesResponses.clear();
        // message of  data changed
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final WeakReference<ClickPhotoCallback> callback;
        private final ImageView imageView;

        public static MyViewHolder newInstance(@NonNull ViewGroup parent, ClickPhotoCallback callback) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_grid_item, parent, false);
            return new MyViewHolder(view, callback);
        }

        public MyViewHolder(@NonNull View itemView, ClickPhotoCallback callback) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgCard);
            this.callback = new WeakReference<>(callback);
        }

        public void bind(ImagesResponse imagesResponse) {
            // ON CLICK ITEM
            imageView.setOnClickListener(view -> {
                ClickPhotoCallback call = callback.get();
                if (call != null) {
                    call.onPhotoClick(imagesResponse);
                }
            });

            Glide.with(itemView.getContext())
                    .load(imagesResponse.getUrls().getRegular())
                    .into(imageView);
        }
    }
}
