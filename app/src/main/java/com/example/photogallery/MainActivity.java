package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity{

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    public Fragment mFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}