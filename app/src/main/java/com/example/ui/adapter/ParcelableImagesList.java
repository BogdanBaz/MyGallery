package com.example.ui.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.api.responses.ImagesResponse;

import java.util.List;

public class ParcelableImagesList implements Parcelable {
    public List<ImagesResponse> getImagesResponses() {
        return imagesResponses;
    }

    public void setImagesResponses(List<ImagesResponse> imagesResponses) {
        this.imagesResponses = imagesResponses;
    }

    private List<ImagesResponse> imagesResponses;

    public ParcelableImagesList(List<ImagesResponse> imagesResponses) {
        this.imagesResponses = imagesResponses;
    }

    protected ParcelableImagesList(Parcel in) {
        imagesResponses = (List<ImagesResponse>) in.readValue(getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(imagesResponses);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableImagesList> CREATOR = new Creator<ParcelableImagesList>() {
        @Override
        public ParcelableImagesList createFromParcel(Parcel in) {
            return new ParcelableImagesList(in);
        }

        @Override
        public ParcelableImagesList[] newArray(int size) {
            return new ParcelableImagesList[size];
        }
    };
}
