package com.example.photogallery.controller;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.photogallery.R;
import com.example.photogallery.model.GalleryItem;
import com.example.photogallery.network.FlickrFetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private PhotoAdapter mPhotoAdapter;
    private List<GalleryItem> mGalleryItems = new ArrayList<>();

    public PhotoGalleryFragment() {
        // Required empty public constructor
    }

    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new PhotoTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        findViews(view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        setUpAdapter(mGalleryItems);
        return view;
    }

    private void setUpAdapter(List<GalleryItem> galleryItems){
        if (isAdded()) {
            mPhotoAdapter = new PhotoAdapter(getContext(), galleryItems);
            mRecyclerView.setAdapter(mPhotoAdapter);
        }
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.Photo_Recycler_view);
    }

    public class PhotoTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            try {
                List<GalleryItem> galleryItems = new FlickrFetcher().fetchItems();
                return galleryItems;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            super.onPostExecute(galleryItems);
            mGalleryItems = galleryItems;
            setUpAdapter(galleryItems);
        }
    }
}