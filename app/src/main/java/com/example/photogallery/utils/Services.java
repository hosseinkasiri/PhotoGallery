package com.example.photogallery.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.photogallery.R;
import com.example.photogallery.controller.PhotoGalleryActivity;
import com.example.photogallery.model.GalleryItem;
import com.example.photogallery.network.FlickrFetcher;
import com.example.photogallery.prefs.QueryPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Services {
    private static final String ACTION_SHOW_NOTIFICATION = "com.example.photogallery.utils_SHOW_NOTIFICATION";
    private static final String EXTRA_REQUEST_CODE = "request_code";
    private static final String EXTRA_NOTIFICATION = "notification";
    private static final String PERM_PRIVATE = "private static final String PERM_PRIVATE";

    public static String getPermPrivate() {
        return PERM_PRIVATE;
    }

    public static String getActionShowNotification() {
        return ACTION_SHOW_NOTIFICATION;
    }

    public static String getExtraRequestCode() {
        return EXTRA_REQUEST_CODE;
    }

    public static String getExtraNotification() {
        return EXTRA_NOTIFICATION;
    }

    public static void pollServerAndSendNotification(Context context, String TAG){
        String query = QueryPreferences.getStoredQuery(context);
        String lastId = QueryPreferences.getLastId(context);
        List<GalleryItem> galleryItems = new ArrayList<>();
        try {
            if (query == null)
                galleryItems = new FlickrFetcher().fetchPopular();
            else
                galleryItems = new FlickrFetcher().searchGalleryItems(query);
        } catch (IOException e) {
            Log.e(TAG, "error in get from server : ", e);
        }
        if (galleryItems.size() == 0)
            return;

        if (galleryItems.get(0).getId().equals(lastId)){
            Log.d(TAG, "old result");
        }else {
            Log.d(TAG, "new result");
            String channelId = context.getString(R.string.channel_id);
            Intent i = PhotoGalleryActivity.newIntent(context);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
            Notification notification = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle(context.getString(R.string.new_pictures_title))
                    .setContentText(context.getString(R.string.new_pictures_text))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            Intent intent = new Intent(ACTION_SHOW_NOTIFICATION);
            intent.putExtra(EXTRA_REQUEST_CODE, 0);
            intent.putExtra(EXTRA_NOTIFICATION,notification);
//            context.sendBroadcast(intent, PERM_PRIVATE);
            context.sendOrderedBroadcast(intent,
                    PERM_PRIVATE,
                    null,
                    null,
                    Activity.RESULT_OK,
                    null,
                    null);
        }
        QueryPreferences.setLastId(context, galleryItems.get(0).getId());
    }
}