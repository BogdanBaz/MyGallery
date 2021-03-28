package com.example.api.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.api.responses.SearchingImages;
import com.example.api.retrofit.ApiClient;
import com.example.ui.screens.mainscreen.MainScreen;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.ui.screens.mainscreen.MainScreen.TAG;
import static com.example.ui.screens.mainscreen.MainScreen.showMessage;

public class SearchList {
    private int page;
    private String query;

    private final MutableLiveData<SearchingImages> searchingResponsesList;

    public SearchList(MutableLiveData<SearchingImages> liveData) {
        searchingResponsesList = liveData;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setPage(int page) {
        this.page = page;
        apiCall();
    }

    public void clearData() {
        searchingResponsesList.postValue(null);
    }

    public void apiCall() {

        Single<SearchingImages> imagesResponse = ApiClient.getInterface().searchImages(page, MainScreen.perPage, query);
        imagesResponse.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SearchingImages>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull SearchingImages searchingImages) {
                        searchingResponsesList.postValue(searchingImages);
                    }

                    @Override
                    public void onError(@NotNull Throwable t) {
                        Log.d(TAG, "ERROR:  " + t.getMessage());
                        showMessage("Network error...");
                    }
                });
    }
}