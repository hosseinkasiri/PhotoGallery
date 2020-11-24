package com.example.photogallery.network;

import android.net.Uri;
import android.util.Log;

import com.example.photogallery.model.Gallery;
import com.example.photogallery.model.GalleryItem;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlickrFetcher {
    private static final String TAG = "FlickrFetcher";
    private static final String API_KEY = "1aa9bb82f337d3374b2ea53357cab169";

    private Uri endPoint = Uri.parse("https://www.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("method", "flickr.photos.getRecent")
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .appendQueryParameter("user_id", "34427466731@N01")
            .build();

    private enum FlickerMethods{
        popular,
        search
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            if (httpURLConnection.getResponseCode() != httpURLConnection.HTTP_OK)
                throw new IOException(httpURLConnection.getResponseMessage() + "with" + url);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int readSize = 0;
            while ((readSize = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, readSize);
            }
            outputStream.close();
            return outputStream.toByteArray();

        }catch (MalformedURLException e){
            Log.e(TAG, "failed to fecth url: ", e);
        }finally {
            httpURLConnection.disconnect();
        }
        return null;
    }
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> downloadGalleryItems(String url) throws IOException {
//        String url = Uri.parse("https://www.flickr.com/services/rest/")
//                .buildUpon()
//                .appendQueryParameter("method", "flickr.photos.getRecent")
//                .appendQueryParameter("api_key", API_KEY)
//                .appendQueryParameter("format", "json")
//                .appendQueryParameter("nojsoncallback", "1")
//                .appendQueryParameter("extras", "url_s")
//                .build().toString();
        String result = getUrlString(url);
        List<GalleryItem> galleryItems = new ArrayList<>();
        try {
            JSONObject jsonBody = new JSONObject(result);
            JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
            Gson gson = new Gson();
            Gallery gallery = gson.fromJson(photosJsonObject.toString(), Gallery.class);
            galleryItems = gallery.getGalleryItems();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return galleryItems;
    }

    public List<GalleryItem> fetchPopular() throws IOException {
        String url = buildUrl(FlickerMethods.popular, null);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchGalleryItems(String query) throws IOException {
        String url = buildUrl(FlickerMethods.search, query);
        return downloadGalleryItems(url);

    }

    private String buildUrl(FlickerMethods methods, String query){
        Uri.Builder builder = endPoint.buildUpon();
        switch (methods){
            case popular:
                builder.appendQueryParameter("method", "flickr.photos.getPopular");
                break;
            case search:
                builder.appendQueryParameter("method", "flickr.photos.search").
                        appendQueryParameter("text", query);
            default:
                return null;
        }
        return builder.build().toString();
    }
}
