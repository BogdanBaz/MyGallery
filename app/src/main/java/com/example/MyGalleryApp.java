package com.example;

import android.app.Application;

public class MyGalleryApp extends Application {

   public static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
