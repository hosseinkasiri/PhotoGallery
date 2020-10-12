package com.example.photogallery.model;

public class GalleryItem {
    private String mId;
    private String mCaption;
    private String mUrl;

    public GalleryItem(String id, String caption, String url) {
        mId = id;
        mCaption = caption;
        mUrl = url;
    }

    public GalleryItem() {
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
