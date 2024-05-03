package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class ImageDisplayFragment extends Fragment {

    private ImageView imageView;
    private String imageUrl;

    public ImageDisplayFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_display, container, false);
        imageView = view.findViewById(R.id.imageView);
        // Load the image into the ImageView using Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(requireContext())
                    .load(imageUrl)
                    .into(imageView);
        }
        return view;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

