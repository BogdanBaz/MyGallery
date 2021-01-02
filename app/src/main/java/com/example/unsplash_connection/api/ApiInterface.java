package com.example.unsplash_connection.api;

import com.example.response_processing.ImagesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("photos")
    Call<List<ImagesResponse>> getAllImages();

}
