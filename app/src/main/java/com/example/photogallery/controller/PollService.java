package com.example.photogallery.controller;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.photogallery.R;
import com.example.photogallery.controller.utils.Services;
import com.example.photogallery.model.GalleryItem;
import com.example.photogallery.network.FlickrFetcher;
import com.example.photogallery.prefs.QueryPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PollService extends IntentService {

    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL = TimeUnit.MINUTES.toMillis(1);

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, PollService.class);
        return intent;
    }

    public static void setServiceAlarm(Context context, boolean isOn){
        Intent i = newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        }else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isAlarmOn(Context context){
        Intent i = newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,
                0,
                i,
                PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableAndConnected())
            return;
        Services.pollServerAndSendNotification(this, TAG);
    }

    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}