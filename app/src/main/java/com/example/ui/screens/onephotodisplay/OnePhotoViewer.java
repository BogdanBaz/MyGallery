package com.example.ui.screens.onephotodisplay;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.mygallery.R;
import com.example.ui.DownloadPhoto;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OnePhotoViewer extends AppCompatActivity implements View.OnClickListener {

    private String selectedPhotoUrl , selectedPhotoId;
    private Button btnShare, btnDownload;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_photo_viewer);

        btnShare = findViewById(R.id.btnShare);
        btnDownload = findViewById(R.id.btnDownload);
        SubsamplingScaleImageView scaleImageView = findViewById(R.id.imageView);
        scaleImageView.setOnClickListener(this);

        btnShare.setOnClickListener(this);
        btnDownload.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            selectedPhotoId = extras.getString("selectedPhotoId");
            selectedPhotoUrl = extras.getString("selectedPhoto");
            URL url = new URL(selectedPhotoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            scaleImageView.setImage(ImageSource.bitmap(myBitmap));

        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageView:
                v.clearAnimation();
                showButtons();
                break;

            case R.id.btnDownload:
                DownloadPhoto downloadPhoto = new DownloadPhoto(selectedPhotoUrl, selectedPhotoId);
                downloadPhoto.downloadPhoto(this);
                Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnShare:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showButtons() {
        btnDownload.setVisibility(View.VISIBLE);
        btnShare.setVisibility(View.VISIBLE);
        btnDownload.animate().alpha(1f).setDuration(1000);
        btnShare.animate().alpha(1f).setDuration(1000);

        new Handler().postDelayed(() -> {
            btnDownload.animate().alpha(0f).setDuration(1250);
            btnShare.animate().alpha(0f).setDuration(1250);
        }, 3000);
    }
}

