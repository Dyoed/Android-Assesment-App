package com.joedsantiago.stratpoint.androidassesment.image;

import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by dyoed on 10/31/15.
 */
public class ImageRequest {

    private ImageLoader mImageLoader;
    private String mUrl;
    private boolean mResetView = true;
    private WeakReference<ImageView> mImgView;

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public boolean isResetView() {
        return mResetView;
    }

    public String getUrl() {
        return mUrl;
    }

    public ImageView getImgView() {
        return mImgView.get();
    }

    public ImageRequest(ImageLoader imageLoader, String url) {
        mImageLoader = imageLoader;
        mUrl = url;
    }

    public ImageRequest to(ImageView imageView){
        mImgView = new WeakReference<>(imageView);
        return this;
    }

    public ImageRequest resetViewBeforeLoading(boolean reset){
        mResetView = reset;
        return this;
    }

    public void start(){
        mImageLoader.start(this);
    }

}
