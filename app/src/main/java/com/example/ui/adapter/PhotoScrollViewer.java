package com.example.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.api.responses.ImagesResponse;
import com.example.api.responses.SearchingImages;
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
    private String query;
    Context context;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;
    private boolean isLoading = true;
    private boolean isSearch = false;
    ProgressBar progressBar;
    private static final String TAG = "myLogs";
    private static final int MAX_PAGE = 5;

    public boolean getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }

    public PhotoScrollViewer(Context context) {
        this.context = context;
        recyclerView = ((Activity) context).findViewById(R.id.recyclerView);
        progressBar = ((Activity) context).findViewById(R.id.progressBar);
        layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onPhotoClick(ImagesResponse imagesResponse) {
        Intent intent = new Intent(context, OnePhotoViewer.class);
        Bundle extras = new Bundle();
        extras.putString("selectedPhoto", imagesResponse.getUrls().getRegular());
        extras.putString("selectedPhotoId", imagesResponse.getId());
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    public void getAllImages(int page) {


        if (page < MAX_PAGE) {
            progressBar.setVisibility(View.VISIBLE);
            Call<List<ImagesResponse>> imagesResponse = ApiClient.getInterface().getAllImages(page, perPage);
            imagesResponse.enqueue(new Callback<List<ImagesResponse>>() {
                @Override
                public void onResponse(@NotNull Call<List<ImagesResponse>> call, @NotNull Response<List<ImagesResponse>> response) {
                    /*if (response.isSuccessful())*/
                    {
                        progressBar.setVisibility(View.GONE);
                        imagesResponses = response.body();
                        if (page == 1) {
                            photoAdapter = new PhotoAdapter(imagesResponses, PhotoScrollViewer.this);
                            recyclerView.setAdapter(photoAdapter);
                            pageScrolling();
                        } else {
                            photoAdapter.addImages(imagesResponses);
                        }

                    }/* else {
                    Toast.makeText(context, "Something going wrong...", Toast.LENGTH_SHORT).show();
                }*/
                    pageScrolling();
                }

                @Override
                public void onFailure(@NotNull Call<List<ImagesResponse>> call, @NotNull Throwable t) {
                    String message = t.getLocalizedMessage();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });
        }

        Log.d(TAG, "totalItem " + totalItemCount + " , prevTotal " + previousTotal + " , Page " + page);

        if (page == MAX_PAGE)
            Toast.makeText(context, "End of list. Maybe Y'll search something?..", Toast.LENGTH_LONG).show();
    }


    public void searchImages(int page, String query) {
        if (page < MAX_PAGE) {

            this.query = query;

            progressBar.setVisibility(View.VISIBLE);
            Call<SearchingImages> searchingImages = ApiClient.getInterface().searchImages(page, perPage, query);
            searchingImages.enqueue(new Callback<SearchingImages>() {
                @Override
                public void onResponse(Call<SearchingImages> call, Response<SearchingImages> response) {
                    /*if (response.isSuccessful())*/
                    {
                        progressBar.setVisibility(View.GONE);
                        imagesResponses = response.body().getResults();
                        if (page == 1) {
                            photoAdapter = new PhotoAdapter(imagesResponses, PhotoScrollViewer.this);
                            recyclerView.setAdapter(photoAdapter);
                            pageScrolling();
                        } else {
                            photoAdapter.addImages(imagesResponses);
                        }
                    }/* else {
                        Toast.makeText(context, "Something going wrong..." + response.message(), Toast.LENGTH_SHORT).show();
                    }*/
                    pageScrolling();

                    Log.d(TAG, "IN SEARCH _  totalItem " + totalItemCount + " , prevTotal " + previousTotal + " , Page " + page);

                }

                @Override
                public void onFailure(Call<SearchingImages> call, Throwable t) {
                    String message = t.getLocalizedMessage();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (page == MAX_PAGE)
            Toast.makeText(context, "End of list. Maybe Y'll search something else?..", Toast.LENGTH_LONG).show();
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
                        Log.d(TAG, "_IN dy > 0");
                    }
                }

                if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + perPage)) {
                    page++;
                    isLoading = true;
                    if (!isSearch) {
                        getAllImages(page);

                    } else {
                        searchImages(page, query);
                    }

                }
            }

        });
        Log.d(TAG, Boolean.toString(isLoading));
    }

    public void resetViewing() {
        layoutManager.removeAllViews();
        imagesResponses.clear();
        this.previousTotal = 0;
        this.page = 1;
    }
}
