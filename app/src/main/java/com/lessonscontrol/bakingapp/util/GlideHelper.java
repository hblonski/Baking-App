package com.lessonscontrol.bakingapp.util;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

public final class GlideHelper {

    private  GlideHelper() {
        //Empty
    }

    @SuppressLint("CheckResult")
    public static void loadImageIntoImageView(View view,
                                              final ImageView imageView,
                                              String imageURL,
                                              Integer errorImage) {
        RequestBuilder builder = Glide.with(view).load(imageURL);
        if (errorImage != null) {
            builder.error(errorImage);
        }
        builder.into(imageView);
    }
}
