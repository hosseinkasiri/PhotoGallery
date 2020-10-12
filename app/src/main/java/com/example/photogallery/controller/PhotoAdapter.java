package com.example.photogallery.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photogallery.model.GalleryItem;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder>{

    private Context mContext;
    private List<GalleryItem> mGalleryItems;

    public void setGalleryItems(List<GalleryItem> galleryItems) {
        mGalleryItems = galleryItems;
    }

    public PhotoAdapter(Context context, List<GalleryItem> galleryItems) {
        mContext = context;
        mGalleryItems = galleryItems;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        GalleryItem galleryItem = mGalleryItems.get(position);
        holder.bind(galleryItem);
    }

    @Override
    public int getItemCount() {
        return mGalleryItems.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder{

        private TextView mTitleTextView;
        private GalleryItem mGalleryItem;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(GalleryItem galleryItem){
            mTitleTextView.setText(galleryItem.getCaption());
            mGalleryItem = galleryItem;
        }
    }
}
