package com.example.photogallery.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.photogallery.R;
import com.example.photogallery.model.Gallery;
import com.example.photogallery.model.GalleryItem;
import com.example.photogallery.network.FlickrFetcher;
import com.example.photogallery.prefs.QueryPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private List<GalleryItem> mGalleryItems = new ArrayList<>();
    private GridLayoutManager mLayoutManager;

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
        setHasOptionsMenu(true);
        updateItem();
        Intent intent = PollService.newIntent(getContext());
        getActivity().startService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        findViews(view);
        mLayoutManager =  new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setUpAdapter(mGalleryItems);
        return view;
    }

    private void setUpAdapter(List<GalleryItem> galleryItems){
        if (isAdded()) {
            if (mPhotoAdapter == null) {
                mPhotoAdapter = new PhotoAdapter(getContext(), galleryItems);
                mRecyclerView.setAdapter(mPhotoAdapter);
            }
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);
        MenuItem togglePollingItem = menu.findItem(R.id.menu_item_toggle_polling);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                QueryPreferences.setStoredQuery(getContext(), query);
                updateItem();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery(QueryPreferences.getStoredQuery(getContext()), false);
            }
        });
        if (isScheduleOrServiceOn())
            togglePollingItem.setTitle(R.string.stop);
        else
            togglePollingItem.setTitle(R.string.start);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_search:
                return true;
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(getContext(), null);
                updateItem();
                return true;
            case R.id.menu_item_toggle_polling:
                setScheduleOrAlarm(isScheduleOrServiceOn());
                getActivity().invalidateOptionsMenu();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateItem() {
        new PhotoTask().execute(QueryPreferences.getStoredQuery(getContext()));
    }

    private void setScheduleOrAlarm(boolean isOn){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            PollJobService.schedulerService(getContext(), isOn);
        else
            PollService.isAlarmOn(getContext());
    }

    private boolean isScheduleOrServiceOn(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return PollJobService.isScheduled(getContext());
        else
            return PollService.isAlarmOn(getContext());
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.Photo_Recycler_view);
    }

    public class PhotoTask extends AsyncTask<String, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(String... params) {
            String query = null;
            List<GalleryItem> galleryItems;
            try {
                if (params != null && params.length > 0)
                    query = params[0];
                if (query == null)
                    galleryItems = new FlickrFetcher().fetchPopular();
                else
                    galleryItems = new FlickrFetcher().searchGalleryItems(query);
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
            mGalleryItems.addAll(galleryItems);
            setUpAdapter(mGalleryItems);
        }
    }
}