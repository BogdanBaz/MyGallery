package com.example.api.retrofit;

import com.example.api.responses.ImagesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("photos")
    Call<List<ImagesResponse>> getAllImages(@Query("page") Integer page , @Query("per_page") Integer perPage);

}
