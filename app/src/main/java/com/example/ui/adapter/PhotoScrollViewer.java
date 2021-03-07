package com.example.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.api.responses.ImagesResponse;
import com.example.api.responses.SearchingImages;
import com.example.api.viewModel.EndlessRecyclerOnScrollListener;
import com.example.api.viewModel.ImagesListViewModel;
import com.example.api.viewModel.SearchListViewModel;
import com.example.mygallery.R;
import com.example.ui.screens.onephotodisplay.OnePhotoViewer;

import java.util.List;

public class PhotoScrollViewer extends ViewModel implements ClickPhotoCallback {

    public static final int perPage = 20;
    PhotoAdapter photoAdapter;
    private final GridLayoutManager layoutManager;
    @SuppressLint("StaticFieldLeak")
    private final RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    Context context;
    private boolean isSearch = false;
    @SuppressLint("StaticFieldLeak")
    ProgressBar progressBar;
    public static final String TAG = "myLogs";
    private Observer observer;

    private ImagesListViewModel imgListViewModel;
    private SearchListViewModel srchListViewModel;

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


    public void getAllImages() {
        if (srchListViewModel != null) {
            srchListViewModel.getSearchingResponsesList().removeObserver(observer);
            srchListViewModel.clearData();
        }
        imgListViewModel = new ImagesListViewModel();

        observer = new Observer<List<ImagesResponse>>() {
            @Override
            public void onChanged(List<ImagesResponse> imagesResponses) {
                if (photoAdapter == null) {
                    setAdapter(imagesResponses);
                } else {
                    photoAdapter.addImages(imagesResponses);
                    progressBar.setVisibility(View.GONE);
                }
            }
        };

        imgListViewModel.observe((AppCompatActivity) context, observer);
        Log.d(TAG, "beforeApiCall  phAdapter is- " + photoAdapter);
        imgListViewModel.setPage(1);
        pageScrolling();
    }

    public void searchImages(String query) {
        imgListViewModel.removeObservers((AppCompatActivity) context);
        imgListViewModel.clearData();
        srchListViewModel = new ViewModelProvider((AppCompatActivity) context).get(SearchListViewModel.class);

        observer = new Observer<SearchingImages>() {
            @Override
            public void onChanged(SearchingImages searchingImages) {

                if (photoAdapter == null) {
                    if (searchingImages == null)
                        return;
                    setAdapter(searchingImages.getResults());
                } else {
                    photoAdapter.addImages(searchingImages.getResults());
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        srchListViewModel.setQuery(query);
        srchListViewModel.getSearchingResponsesList().observe((AppCompatActivity) context, observer);
        Log.d(TAG, "beforeApiCall   phAdapter is- " + photoAdapter);
        srchListViewModel.setPage(1);
        pageScrolling();
    }

    private void setAdapter(List<ImagesResponse> imgResponse) {
        photoAdapter = new PhotoAdapter(imgResponse, PhotoScrollViewer.this);
        recyclerView.swapAdapter(photoAdapter, true);
        Log.d(TAG, "setAdapter");
    }

    public void pageScrolling() {

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int nextPage) {
                progressBar.setVisibility(View.VISIBLE);

                if (!isSearch) {
                    Log.d(TAG, "!!! SET Page in SCROLL % " + nextPage);
                    imgListViewModel.setPage(nextPage);
                } else if (isSearch) {
                    Log.d(TAG, "!!! SET Page in SCROLL % " + nextPage);
                    srchListViewModel.setPage(nextPage);
                }
            }
        });
    }

    public void resetViewing() {
        recyclerView.clearOnScrollListeners();

        if (photoAdapter != null) {
            photoAdapter.clearData();
            photoAdapter = null;
        }
    }
}
