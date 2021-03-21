package com.example.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    public static PhotoAdapter photoAdapter;
    private final GridLayoutManager layoutManager;
    @SuppressLint("StaticFieldLeak")
    private final RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private boolean isSearch = false;
    @SuppressLint("StaticFieldLeak")
    ProgressBar progressBar;
    public static final String TAG = "myLogs";
    private Observer observer;

    private ImagesListViewModel imgListViewModel;
    private SearchListViewModel srchListViewModel;
    private EndlessRecyclerOnScrollListener onScrollListener;

    public boolean getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }

    public PhotoScrollViewer(Context context) {
        PhotoScrollViewer.context = context;
        recyclerView = ((Activity) context).findViewById(R.id.recyclerView);
        progressBar = ((Activity) context).findViewById(R.id.progressBar);
        this.layoutManager = getLayoutManager();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private GridLayoutManager getLayoutManager() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int numOfColumns = (int) (screenWidthDp / 250 + 0.5);
        return new GridLayoutManager(context, numOfColumns);
    }

    public int getCurrentPage() {
        return onScrollListener.getPage();
    }

    public void setCurrentPage(int page) {
        onScrollListener.setPage(page);
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

    public static void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public void getAllImages() {
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

        if (photoAdapter == null) {
            imgListViewModel.setPage(1);
        } else {
            setAdapter(null);
        }
        pageScrolling();
    }

    public void searchImages(String query) {
        srchListViewModel = new ViewModelProvider((AppCompatActivity) context).get(SearchListViewModel.class);

        observer = new Observer<SearchingImages>() {
            @Override
            public void onChanged(SearchingImages searchingImages) {
                if (photoAdapter == null) {
                    if (searchingImages == null)
                        return;
                    else {
                        setAdapter(searchingImages.getResults());
                        if (searchingImages.getResults().size() == 0) {
                            showMessage("No results for " + "\"" + query + "\"");
                        }
                    }
                } else {
                    photoAdapter.addImages(searchingImages.getResults());
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        srchListViewModel.setQuery(query);
        srchListViewModel.getSearchingResponsesList().observe((AppCompatActivity) context, observer);

        if (photoAdapter == null) {
            srchListViewModel.setPage(1);
        } else {
            setAdapter(null);
        }
        pageScrolling();
    }

    private void setAdapter(List<ImagesResponse> imgResponse) {
        if (imgResponse != null) {
            photoAdapter = new PhotoAdapter(imgResponse, PhotoScrollViewer.this);
        }
        recyclerView.swapAdapter(photoAdapter, true);
    }


    public void pageScrolling() {
        onScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int nextPage) {
                progressBar.setVisibility(View.VISIBLE);

                Log.d(TAG, "!!! SET Page in SCROLL % " + nextPage);
                if (!isSearch) {
                    imgListViewModel.setPage(nextPage);
                } else {
                    srchListViewModel.setPage(nextPage);
                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
    }

    public void resetViewing() {
        recyclerView.clearOnScrollListeners();

        if (imgListViewModel != null) {
            imgListViewModel.removeObservers((AppCompatActivity) context);
            imgListViewModel.clearData();
        }

        if (srchListViewModel != null) {
            srchListViewModel.getSearchingResponsesList().removeObservers((AppCompatActivity) context);
            srchListViewModel.clearData();
        }

        if (photoAdapter != null) {
            photoAdapter.clearData();
            photoAdapter = null;
        }
    }
}
