package com.example.api.retrofit;

import com.example.api.responses.ImagesResponse;
import com.example.api.responses.SearchingImages;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("photos")
    Call<List<ImagesResponse>> getAllImages(@Query("page") Integer page , @Query("per_page") Integer perPage);

    @GET ("search/photos")
    Call<SearchingImages> searchImages(@Query("page") Integer page ,
                                       @Query("per_page") Integer perPage , @Query("query") String query);

}
