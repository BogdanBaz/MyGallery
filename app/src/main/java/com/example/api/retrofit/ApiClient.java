package com.example.api.retrofit;

import com.example.api.data.UnsplashAuthorizationData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiClient {

private static Retrofit retrofit = null;

        public static Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HeaderInterceptor(UnsplashAuthorizationData.getAccessKey())).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(UnsplashAuthorizationData.getBaseUrl())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static ApiInterface getInterface() {
        return getRetrofit().create(ApiInterface.class);
    }
}
