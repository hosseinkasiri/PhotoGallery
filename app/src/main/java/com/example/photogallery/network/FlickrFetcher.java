package com.example.photogallery.network;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FlickrFetcher {
    private static final String TAG = "FlickrFetcher";

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
}
