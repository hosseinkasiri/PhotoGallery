package com.example.photogallery.controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.photogallery.utils.Services;

public class VisibleFragment extends Fragment {
    private static final String TAG = "visible fragment";
    private ShowNotificationReceiver mOnShowNotificationReceiver;

    public VisibleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnShowNotificationReceiver = new ShowNotificationReceiver();
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(Services.getActionShowNotification());
        getActivity().registerReceiver(mOnShowNotificationReceiver, intentFilter,
                Services.getPermPrivate(), null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mOnShowNotificationReceiver);
    }

    private class ShowNotificationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "visible fragment");
            setResultCode(Activity.RESULT_CANCELED);
        }
    }
}