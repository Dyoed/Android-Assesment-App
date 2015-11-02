package com.joedsantiago.stratpoint.androidassesment.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by dyoed on 10/31/15.
 */
public class ImageDisplayTask implements Runnable {

    private WeakReference<ImageView> mImgView;
    private WeakReference<Bitmap> mBitmap;

    public ImageDisplayTask(ImageView imgView, Bitmap bitmap) {
        mImgView = new WeakReference<>(imgView);
        mBitmap = new WeakReference<>(bitmap);
    }

    @Override
    public void run() {

        if (mImgView.get() != null && mBitmap.get() != null && !mBitmap.get().isRecycled()) {
            mImgView.get().setImageBitmap(mBitmap.get());
        }

    }
}
