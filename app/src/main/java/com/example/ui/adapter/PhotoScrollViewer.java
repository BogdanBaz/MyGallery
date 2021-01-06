package com.example.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.api.responses.ImagesResponse;
import com.example.api.retrofit.ApiClient;
import com.example.mygallery.R;
import com.example.ui.screens.onephotodisplay.OnePhotoViewer;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoScrollViewer implements ClickPhotoCallback {

    private int page = 1;
    private final int perPage = 20;
    PhotoAdapter photoAdapter;
    private final GridLayoutManager layoutManager;
    private final RecyclerView recyclerView;
    private List<ImagesResponse> imagesResponses;
    Context context;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;
    private boolean isLoading = true;

    public PhotoScrollViewer(Context context) {
        this.context = context;
        recyclerView = ((Activity) context).findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onPhotoClick(ImagesResponse imagesResponse) {
        context.startActivity(new Intent(context, OnePhotoViewer.class)
                .putExtra("selectedPhoto", imagesResponse.getUrls().getRegular()));
    }

    public void getAllImages(int page) {
        Call<List<ImagesResponse>> imagesResponse = ApiClient.getInterface().getAllImages(page, perPage);
        imagesResponse.enqueue(new Callback<List<ImagesResponse>>() {
            @Override
            public void onResponse(@NotNull Call<List<ImagesResponse>> call, @NotNull Response<List<ImagesResponse>> response) {
                String message;
                if (response.isSuccessful()) {
                    message = "Page " + page + " is loaded";
                    imagesResponses = response.body();
                    if (page == 1) {
                        photoAdapter = new PhotoAdapter(imagesResponses, PhotoScrollViewer.this);
                        recyclerView.setAdapter(photoAdapter);
                        pageScrolling();
                    } else {
                        photoAdapter.addImages(imagesResponses);
                    }

                } else {
                    message = "Something going wrong...";
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                pageScrolling();
            }

            @Override
            public void onFailure(@NotNull Call<List<ImagesResponse>> call, @NotNull Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void pageScrolling() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal)
                            isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + perPage)) {
                    page++;
                    getAllImages(page);
                    isLoading = true;
                }
            }
        });
    }
}
