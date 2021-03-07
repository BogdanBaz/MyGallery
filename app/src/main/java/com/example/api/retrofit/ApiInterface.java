package com.example.api.retrofit;

import com.example.api.responses.ImagesResponse;
import com.example.api.responses.SearchingImages;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("photos")
    Single<List<ImagesResponse>> getAllImages(@Query("page") Integer page, @Query("per_page") Integer perPage);

    @GET("search/photos")
    Single<SearchingImages> searchImages(@Query("page") Integer page, @Query("per_page") Integer perPage,
                                                            @Query("query") String query);


}
