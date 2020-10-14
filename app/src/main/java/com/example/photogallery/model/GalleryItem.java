package com.example.photogallery.model;

import com.google.gson.annotations.SerializedName;

public class GalleryItem {
    @SerializedName("id")
    private String mId;
    @SerializedName("title")
    private String mCaption;
    @SerializedName("url_s")
    private String mUrl;

    public GalleryItem(String id, String caption, String url) {
        mId = id;
        mCaption = caption;
        mUrl = url;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
