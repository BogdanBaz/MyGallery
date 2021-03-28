package com.example.api.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.api.responses.ImagesResponse;
import com.example.api.retrofit.ApiClient;
import com.example.ui.screens.mainscreen.MainScreen;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.ui.screens.mainscreen.MainScreen.showMessage;


public class ImagesList {

    private int page;

    private final MutableLiveData<List<ImagesResponse>> searchingResponsesList;

    public ImagesList(MutableLiveData<List<ImagesResponse>> liveData) {
        searchingResponsesList = liveData;
    }

    public void setPage(int page) {
        this.page = page;
        apiCall();
    }

    public void clearData() {
        searchingResponsesList.setValue(null);
    }

    public void apiCall() {

        Single<List<ImagesResponse>> imagesResponse = ApiClient.getInterface().getAllImages(page, MainScreen.perPage);
        imagesResponse.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ImagesResponse>>() {

                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<ImagesResponse> imagesResponses) {
                        searchingResponsesList.postValue(imagesResponses);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d(MainScreen.TAG, "ERROR:  " + e.toString());
                        showMessage("Network error...");
                    }
                });
    }
}
