package com.example.api.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
public class ImagesResponse  {
    @JsonProperty("urls")
    private Urls urls;
    @JsonProperty("id")
    private String id;

    public Urls getUrls() {
        return urls;
    }
    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
