package com.example.ui.screens.mainscreen;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.api.responses.ImagesResponse;
import com.example.api.responses.SearchingImages;
import com.example.api.viewModel.ImagesList;
import com.example.api.viewModel.SearchList;

import java.util.List;

public class MainViewModel extends ViewModel {

    MutableLiveData<SearchingImages> searchingLiveData = new MutableLiveData<>();
    MutableLiveData<List<ImagesResponse>> imagesLiveData = new MutableLiveData<>();

    private final SearchList searchList = new SearchList(searchingLiveData);
    private final ImagesList imagesList = new ImagesList(imagesLiveData);


    public void setPage(int page) {
        imagesList.setPage(page);
    }

    public void clearData() {
        imagesList.clearData();
    }

    public void setSearchQuery(String query) {
        searchList.setQuery(query);
    }

    public void setSearchPage(int page) {
        searchList.setPage(page);
    }

    public void clearSearchData() {
        searchList.clearData();
    }

    @Override
    protected void onCleared() {
        clearData();
        clearSearchData();

        super.onCleared();
    }
}

