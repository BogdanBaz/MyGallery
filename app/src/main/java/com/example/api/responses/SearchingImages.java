package com.example.api.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchingImages {
    @SerializedName("results")
    @Expose
    private List<ImagesResponse> results = null;

    public List<ImagesResponse> getResults() {
        return results;
    }
}
