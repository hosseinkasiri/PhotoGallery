package com.example.photogallery.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photogallery.R;
import com.example.photogallery.model.GalleryItem;
import com.squareup.picasso.Picasso;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_gallery, parent, false);
        return new PhotoHolder(view);
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

        private ImageView mGalleryImageView;
        private GalleryItem mGalleryItem;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mGalleryImageView = itemView.findViewById(R.id.list_item_image_view);
        }

        public void bind(GalleryItem galleryItem){
            mGalleryItem = galleryItem;
            Picasso.get()
                    .load(mGalleryItem.getUrl())
                    .placeholder(R.drawable.ic_default_picture)
                    .into(mGalleryImageView);
        }
    }
}
