package com.example.api.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.api.responses.SearchingImages;
import com.example.api.retrofit.ApiClient;
import com.example.ui.adapter.PhotoScrollViewer;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.ui.adapter.PhotoScrollViewer.TAG;

public class SearchListViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private int page;
    private String query;


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }

    private final MutableLiveData<SearchingImages> searchingResponsesList;

    public SearchListViewModel() {
        searchingResponsesList = new MutableLiveData<>();
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

    public LiveData<SearchingImages> getSearchingResponsesList() {
        return searchingResponsesList;
    }

    public void apiCall() {

        Single<SearchingImages> imagesResponse = ApiClient.getInterface().searchImages(page, PhotoScrollViewer.perPage, query);
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
                    public void onError(Throwable t) {
                        Log.d(TAG, "ERROR:  " + t.getMessage());
                    }
                });
    }
}