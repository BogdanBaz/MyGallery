package com.example.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class SharePhoto {
    private final String url;
    Uri screenshotUri;

    public SharePhoto(String url) {
        this.url = url;
    }

    public void share(Context context) {

        Glide.with(context)
                .asBitmap()
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("image/*");

                        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), resource, "", null);

                        screenshotUri = Uri.parse(path);
                        i.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                        i.setType("image/*");
                        context.startActivity(Intent.createChooser(i, "Share Image"));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    protected void finalize() throws Throwable {
                        super.finalize();
                        try {
                            context.getContentResolver().delete(screenshotUri, null, null);
                            Log.d("LOG", screenshotUri.toString() + " DELETED.");

                        } catch (Exception e) {
                            Log.d("LOG", e.getLocalizedMessage());
                        }
                    }
                });
    }

}
