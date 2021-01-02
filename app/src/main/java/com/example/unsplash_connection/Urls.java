package com.example.unsplash_connection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
public class Urls {
    @SerializedName("regular")
    @Expose
    private String regular;
    public String getRegular() {
        return regular;
    }
}
