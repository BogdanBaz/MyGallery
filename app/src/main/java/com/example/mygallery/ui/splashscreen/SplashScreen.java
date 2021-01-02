package com.example.mygallery.ui.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygallery.R;
import com.example.mygallery.ui.screens.MainScreen;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.my_rotate);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.startAnimation(animation);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this, MainScreen.class);
            startActivity(i);
            finish();
        }, 3000);
    }
}