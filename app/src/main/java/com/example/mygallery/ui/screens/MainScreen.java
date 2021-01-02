package com.example.mygallery.ui.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mygallery.R;
import com.example.mygallery.adapter.CustomAdapter;
import com.example.response_processing.ImagesResponse;
import com.example.unsplash_connection.api.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnSearch;
    EditText editText;
    private List<ImagesResponse> imagesResponses;
    GridView gridView;
/*

    private static MainScreen mainScreen;

    private MainScreen() {
    }

    public static MainScreen getMainScreen() {
        if (mainScreen == null) {
            mainScreen = new MainScreen();
        }
        return mainScreen;
    }
*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSearch = findViewById(R.id.btnSearch);
        editText = findViewById(R.id.editTextSrch);
        btnSearch.setOnClickListener(this);
        gridView = findViewById(R.id.gridView);
        getAllImages();
    }

    @Override
    public void onClick(View v) {
        editText.setVisibility(View.VISIBLE);
    }


    public void getAllImages() {
        Call<List<ImagesResponse>> imagesResponse = ApiClient.getInterface().getAllImages();
        imagesResponse.enqueue(new Callback<List<ImagesResponse>>() {
            @Override
            public void onResponse(Call<List<ImagesResponse>> call, Response<List<ImagesResponse>> response) {
                String message;
                if (response.isSuccessful()) {
                    message = "Success";
                    imagesResponses = response.body();
                    CustomAdapter customAdapter = new CustomAdapter(imagesResponses , MainScreen.this);
                    gridView.setAdapter(customAdapter);

                }
                else {
                    message = "Something going wrong...";
                }
                Toast.makeText(MainScreen.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<ImagesResponse>> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(MainScreen.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
