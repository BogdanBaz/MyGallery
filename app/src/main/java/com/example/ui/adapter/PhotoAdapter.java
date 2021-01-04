package com.example.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mygallery.R;
import com.example.api.responses.ImagesResponse;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private List<ImagesResponse> imagesResponses;
    private Context context;

    public PhotoAdapter(List<ImagesResponse> imagesResponses, Context context) {
        this.imagesResponses = imagesResponses;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_grid_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImagesResponse imagesResponse = imagesResponses.get(position);
        Glide.with(context)
                .load(imagesResponse.getUrls().getRegular())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagesResponses.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgCard);
        }
    }

    public void addImages(List<ImagesResponse> images) {
        for (ImagesResponse response : images) {
            imagesResponses.add(response);
            notifyDataSetChanged();
        }
    }
}
