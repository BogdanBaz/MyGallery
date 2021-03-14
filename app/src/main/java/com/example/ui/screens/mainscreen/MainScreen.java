package com.example.ui.screens.mainscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mygallery.R;
import com.example.ui.adapter.PhotoScrollViewer;

import static com.example.ui.adapter.PhotoScrollViewer.showMessage;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnSearch, btnCancel;
    EditText editText;
    PhotoScrollViewer photoScrollViewer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        btnCancel = findViewById(R.id.btnCancel);
        editText = findViewById(R.id.editTextSrch);
        btnSearch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        photoScrollViewer = new PhotoScrollViewer(MainScreen.this);

        if (getIntent().hasExtra("query")) {

            String searchRequest = getIntent().getStringExtra("query");

            editText.setText(searchRequest);
            editText.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.VISIBLE);

            performSearch(searchRequest);

        } else {
            photoScrollViewer.getAllImages();
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

                photoScrollViewer.resetViewing();
                photoScrollViewer.setIsSearch(false);
                photoScrollViewer.getAllImages();

                editText.getText().clear();
                editText.setVisibility(View.INVISIBLE);
                mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                btnCancel.setVisibility(View.INVISIBLE);
                btnSearch.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void performSearch(String searchRequest) {
        showMessage( "Searching " + " ' " + searchRequest + " ' ...");
        photoScrollViewer.resetViewing();
        photoScrollViewer.setIsSearch(true);
        photoScrollViewer.searchImages(searchRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        photoScrollViewer.resetViewing();
    }
}
