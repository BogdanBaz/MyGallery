package com.example.api.retrofit;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    private final String clientId;

    public HeaderInterceptor(String clientId) {
        this.clientId = clientId;
    }
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("Accept-Version", "v1")
                    .addHeader("Authorization", "Client-ID " + clientId)
                    .build();
            return chain.proceed(request);
    }
}
