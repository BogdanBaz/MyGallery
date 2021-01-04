package com.example.ui.screens.mainscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mygallery.R;
import com.example.ui.adapter.PhotoScrollViewer;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnSearch;
    EditText editText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSearch = findViewById(R.id.btnSearch);
        editText = findViewById(R.id.editTextSrch);
        btnSearch.setOnClickListener(this);
        PhotoScrollViewer photoScrollViewer = new PhotoScrollViewer(MainScreen.this);
        photoScrollViewer.getAllImages(1);

    }

    @Override
    public void onClick(View v) {
        editText.setVisibility(View.VISIBLE);
    }
}
