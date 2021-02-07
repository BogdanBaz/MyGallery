package com.example.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.ui.screens.onephotodisplay.OnePhotoViewer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadPhoto extends OnePhotoViewer {
    public   String selectedPhotoUrl;
    public   String selectedPhotoId;


    public DownloadPhoto(String selectedPhotoUrl, String selectedPhotoId) {
        this.selectedPhotoUrl = selectedPhotoUrl;
        this.selectedPhotoId = selectedPhotoId;
    }
// in view model
    public   void download(Context context) {

        Glide.with(context).asBitmap().load(selectedPhotoUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NotNull Bitmap bitmap, Transition<? super Bitmap> transition) {
                try {
                    File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(myDir, selectedPhotoId + ".jpg"));

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                // by live data
                Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });
    }

}
