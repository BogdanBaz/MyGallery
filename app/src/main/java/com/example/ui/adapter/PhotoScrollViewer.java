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

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
        progressBar.setVisibility(View.VISIBLE);

        Observable<List<ImagesResponse>> imagesResponse = ApiClient.getInterface().getAllImages(page, perPage);
        imagesResponse.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ImagesResponse>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (page > MAX_PAGE) {
                            Toast.makeText(context, "End of list. Maybe Y'll search something?..", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            d.dispose();
                        }
                    }

                    @Override
                    public void onNext(@NotNull List<ImagesResponse> imgResponse) {
                        imagesResponses = imgResponse;
                        if (page == 1) {
                            setAdapter();
                        } else {
                            photoAdapter.addImages(imagesResponses);
                        }
                        pageScrolling();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "ERROR:  " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "COMPLETED!");
                    }
                });
    }

    public void searchImages(int page, String query) {
        this.query = query;
        progressBar.setVisibility(View.VISIBLE);

        Observable<SearchingImages> imagesResponse = ApiClient.getInterface().searchImages(page, perPage, query);
        ;
        imagesResponse.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchingImages>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (page > MAX_PAGE) {
                            Toast.makeText(context, "End of list. Search something ELSE ...", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            d.dispose();
                        }
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull SearchingImages searchingImages) {
                        imagesResponses = searchingImages.getResults();
                        if (page == 1) {
                            setAdapter();
                        } else {
                            photoAdapter.addImages(imagesResponses);
                        }
                        pageScrolling();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "ERROR:  " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "COMPLETED!");
                    }
                });

    }

    private void setAdapter() {
        photoAdapter = new PhotoAdapter(imagesResponses, PhotoScrollViewer.this);
        recyclerView.setAdapter(photoAdapter);
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
                    isLoading = true;
                    if (!isSearch) {
                        getAllImages(page);

                    } else {
                        searchImages(page, query);
                    }

                }
            }
        });
    }

    public void resetViewing() {

        if (photoAdapter != null) {
            photoAdapter.clearData();
        }

        this.previousTotal = 0;
        this.page = 1;
    }
}
