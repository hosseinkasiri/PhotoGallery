package com.example.photogallery.controller;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

public class MainActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    public Fragment mFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}