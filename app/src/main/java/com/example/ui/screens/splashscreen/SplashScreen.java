package com.example.ui.screens.splashscreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygallery.R;
import com.example.ui.screens.mainscreen.MainScreen;

import java.util.List;

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

            Uri uri = getIntent().getData();

            if (uri != null) {
                String s = uri.getEncodedQuery();
                if (s != null) {
                    String[] str = s.split("=");
                    i.putExtra("query", str[str.length - 1]);
                }
            }
            startActivity(i);
            finish();
        }, 3000);
    }
}