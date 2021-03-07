package com.example.api.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.api.responses.ImagesResponse;
import com.example.api.retrofit.ApiClient;
import com.example.ui.adapter.PhotoScrollViewer;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.ui.adapter.PhotoScrollViewer.TAG;

public class ImagesListViewModel extends LiveData<List<ImagesResponse>> {

    private int page;

    public void setPage(int page) {
        this.page = page;
        apiCall();
    }

    public void clearData() {
        setValue(null);
    }

    public void apiCall() {

        Single<List<ImagesResponse>> imagesResponse = ApiClient.getInterface().getAllImages(page, PhotoScrollViewer.perPage);
        imagesResponse.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ImagesResponse>>() {

                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<ImagesResponse> imagesResponses) {
                        setValue(imagesResponses);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d(TAG, "ERROR:  " + e.getMessage());
                    }
                });
    }
}
