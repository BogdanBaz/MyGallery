package com.example.ui.screens.onephotodisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.mygallery.R;
import com.example.ui.GlideApp;

public class OnePhotoViewer extends AppCompatActivity {

    ImageView clickedImage;
    String SelectedPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_photo_viewer);
        clickedImage = findViewById(R.id.clickedImage);

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            SelectedPhotoUrl = (String) intent.getSerializableExtra("selectedPhoto");
            GlideApp.with(this).load(SelectedPhotoUrl).into(clickedImage);
        }
    }
}