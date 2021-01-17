package com.example.api.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @SerializedName("small")
    @Expose
    private String small;
    public String getSmall() {
        return small;
    }

}
