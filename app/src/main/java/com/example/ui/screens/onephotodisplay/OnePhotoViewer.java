package com.example.ui.screens.onephotodisplay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.mygallery.R;
import com.example.ui.DownloadPhoto;
import com.example.ui.SharePhoto;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OnePhotoViewer extends AppCompatActivity implements View.OnClickListener {

    private String selectedPhotoUrl, selectedPhotoId, keyId;
    private Button btnShare, btnDownload;

    private static final int REQUEST_PERMISSION_CODE = 1;
    private static final String DOWNLOAD = "Download";
    private static final String SHARE = "Share";

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
                verifyPermissions(DOWNLOAD);
                break;

            case R.id.btnShare:
                verifyPermissions(SHARE);
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

    private void verifyPermissions(String id) {
        this.keyId = id;
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED) {

            switch (id) {
                case DOWNLOAD:
                    download();
                    break;
                case SHARE:
                    share();
                    break;
            }

        } else {
            //TODO: Request only 1 try?? continue downl/share after accept permission   
            ActivityCompat.requestPermissions(OnePhotoViewer.this,
                    permissions, REQUEST_PERMISSION_CODE);
        }
    }

    private void share() {
        SharePhoto sharePhoto = new SharePhoto(selectedPhotoUrl);
        sharePhoto.share(this);
    }

    private void download() {
        DownloadPhoto downloadPhoto = new DownloadPhoto(selectedPhotoUrl, selectedPhotoId);
        downloadPhoto.download(this);
        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            int grantResultsLength = grantResults.length;
            if (grantResultsLength > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "You denied external storage permission.", Toast.LENGTH_LONG).show();
                return;
            } else verifyPermissions(keyId);
        }
    }
}

