package com.example.ui.screens.mainscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyGalleryApp;
import com.example.api.responses.ImagesResponse;
import com.example.api.responses.SearchingImages;
import com.example.api.viewModel.EndlessRecyclerOnScrollListener;
import com.example.mygallery.R;
import com.example.ui.adapter.ClickPhotoCallback;
import com.example.ui.adapter.PhotoAdapter;
import com.example.ui.screens.onephotodisplay.OnePhotoViewer;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class MainScreen extends AppCompatActivity implements View.OnClickListener, ClickPhotoCallback {
    ImageButton btnSearch, btnCancel;
    EditText editText;
    private String searchRequest;
    private MainViewModel viewModel;
    public static final int perPage = 20;
    public static PhotoAdapter photoAdapter;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private boolean isSearch = false;
    ProgressBar progressBar;
    public static final String TAG = "myLogs";
    private EndlessRecyclerOnScrollListener onScrollListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        initUI(savedInstanceState != null);
        subscribeData();
        btnSearch = findViewById(R.id.btnSearch);
        btnCancel = findViewById(R.id.btnCancel);
        editText = findViewById(R.id.editTextSrch);
        btnSearch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (getIntent().hasExtra("query")) {

            String searchRequest = getIntent().getStringExtra("query");

            editText.setText(searchRequest);
            editText.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.VISIBLE);

            performSearch(searchRequest);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("isSearch")) {
                setIsSearch(true);
                searchImages(savedInstanceState.getString("searchRequest"));
                editText.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            } else {
                getAllImages();
            }
            setCurrentPage(savedInstanceState.getInt("currentPage"));
        } else {
            getAllImages();
        }

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    performSearch(editText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        InputMethodManager mgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {

            case R.id.btnSearch:
                editText.setVisibility(View.VISIBLE);
                editText.requestFocus();
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                btnSearch.setVisibility(View.INVISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                break;

            case R.id.btnCancel:

                resetViewing();
                setIsSearch(false);
                getAllImages();

                editText.getText().clear();
                editText.setVisibility(View.INVISIBLE);
                mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                btnCancel.setVisibility(View.INVISIBLE);
                btnSearch.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void performSearch(String searchRequest) {
        this.searchRequest = searchRequest;
        showMessage("Searching " + "\" " + searchRequest + " \"...");
        resetViewing();
        setIsSearch(true);
        searchImages(searchRequest);
    }

    public boolean getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }

    public void initUI(Boolean isAfterRotation) {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        this.layoutManager = getLayoutManager();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        if (!isAfterRotation || photoAdapter==null) {
            photoAdapter = new PhotoAdapter(this);
        }
        recyclerView.setAdapter(photoAdapter);
    }

    private GridLayoutManager getLayoutManager() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int numOfColumns = (int) (screenWidthDp / 250 + 0.5);
        return new GridLayoutManager(this, numOfColumns);
    }

    public int getCurrentPage() {
        return onScrollListener.getPage();
    }

    public void setCurrentPage(int page) {
        onScrollListener.setPage(page);
    }

    @Override
    public void onPhotoClick(ImagesResponse imagesResponse) {
        Intent intent = new Intent(this, OnePhotoViewer.class);
        Bundle extras = new Bundle();
        extras.putString("selectedPhoto", imagesResponse.getUrls().getRegular());
        extras.putString("selectedPhotoId", imagesResponse.getId());
        intent.putExtras(extras);
        startActivity(intent);
    }

    public static void showMessage(String message) {
        Toast.makeText(MyGalleryApp.instance, message, Toast.LENGTH_LONG).show();
    }


    public void getAllImages() {
        viewModel.setPage(1);
        pageScrolling();
    }

    public void searchImages(String query) {
        viewModel.setSearchQuery(query);
        viewModel.setSearchPage(1);
        pageScrolling();
    }

    private void subscribeData() {
        Observer<List<ImagesResponse>> observer = new Observer<List<ImagesResponse>>() {
            @Override
            public void onChanged(List<ImagesResponse> imagesResponses) {
                if (imagesResponses == null) return;
                photoAdapter.addImages(imagesResponses);
                progressBar.setVisibility(View.GONE);
            }
        };
        Observer<SearchingImages> observerSearch = new Observer<SearchingImages>() {
            @Override
            public void onChanged(SearchingImages searchingImages) {
                if (searchingImages == null) return;
                photoAdapter.addImages(searchingImages.getResults());
                progressBar.setVisibility(View.GONE);
            }
        };

        viewModel.searchingLiveData.observe(this, observerSearch);

        //TODO if (viewModel.imagesLiveData.getValue() === null) { viewModel.setPage(1) }
        // else  { photoAdapter.addImages(viewModel.imagesLiveData.getValue()) }

        viewModel.imagesLiveData.observe(this, observer);
    }

    public void pageScrolling() {
        onScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int nextPage) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "!!! SET Page in SCROLL % " + nextPage);
                if (!isSearch) {
                    viewModel.setPage(nextPage);
                } else {
                    viewModel.setSearchPage(nextPage);
                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
    }

    public  void resetViewing() {
        recyclerView.clearOnScrollListeners();

        viewModel.clearData();
        viewModel.clearSearchData();

        photoAdapter.clearData();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("currentPage", onScrollListener.getPage());
        savedInstanceState.putBoolean("isSearch", isSearch);
        if (isSearch) {
            savedInstanceState.putString("searchRequest", searchRequest);
        }
    }

}
