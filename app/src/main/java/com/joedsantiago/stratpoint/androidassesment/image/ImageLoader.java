package com.joedsantiago.stratpoint.androidassesment.image;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by dyoed on 10/31/15.
 */
public class ImageLoader {

    private static ImageLoader mInstance;
    private ExecutorService mImageExecutor;
    private Handler mImageHandler;
    private ImageCache mImageCache;
    private WeakHashMap<Integer, Future> mTasks;
    private final Map<Integer, String> cacheKeysForImageView = Collections
            .synchronizedMap(new HashMap<Integer, String>());
    private Context mContext;


    private static final String TAG = "ImageLoader";

    public static void init(Context context, int threadCount){
        mInstance = new ImageLoader(context, threadCount);
    }

    private ImageLoader(Context context, int threadCount){
        mContext = context;
        mImageHandler = new Handler(Looper.getMainLooper());
        mImageExecutor = Executors.newFixedThreadPool(threadCount);
        int memClass = ( (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE ) ).getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 2;
        mImageCache = new ImageCache(cacheSize);
        mTasks = new WeakHashMap<>();
    }

    public static ImageRequest load(String url){
        return new ImageRequest(mInstance, url);
    }


    public void start(final ImageRequest imageRequest){
        Future task = mTasks.get(imageRequest.getImgView().hashCode());

        if(task != null){
            task.cancel(false);
            cacheKeysForImageView.remove(imageRequest.getImgView().hashCode());
            Log.d(TAG, "Same image, cancelling task");
        }

        if(imageRequest.isResetView()){
            imageRequest.getImgView().setImageBitmap(null);
        }

        cacheKeysForImageView.put(imageRequest.getImgView().hashCode(), imageRequest.getUrl());

        Future f = mImageExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = imageRequest.getUrl();
                    Bitmap cachedBitmap = mImageCache.get(url);
                    if(cachedBitmap != null && !cachedBitmap.isRecycled()){
                        Log.d(TAG, "Cached image found for "+url);
                        mImageHandler.post(new ImageDisplayTask(imageRequest.getImgView(), cachedBitmap));
                    }
                    //TODO Implement diskCache
                    else{
                        Log.d(TAG, "No cached image found for " + url + ", downloading image...");
                        Bitmap bitmap = BitmapFactory.decodeStream(new ImageDownloader(url).download());
                        mImageCache.put(url, bitmap);
                        //Check if the imageview matches the URL, for managing view reuse
                        if(imageRequest.getImgView() != null &&
                                imageRequest.getUrl().equals(cacheKeysForImageView.get(imageRequest.getImgView().hashCode()))){
                            Log.d(TAG, "Image download complete, displaying " + url );

                            mImageHandler.post(new ImageDisplayTask(imageRequest.getImgView(), bitmap));
                        }

                    }
                    mTasks.remove(url);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

       mTasks.put(imageRequest.getImgView().hashCode(), f);
    }

    public void cancelAll(){

    }

    public void cancel(){

    }

    private void cacheImage(){

    }

    private Bitmap loadFromCache(){
        return null;
    }
}
