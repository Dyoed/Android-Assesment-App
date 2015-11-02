package com.joedsantiago.stratpoint.androidassesment.image;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dyoed on 10/31/15.
 */
public class ImageDownloader {

    private String mUrl;

    public ImageDownloader(String mUrl) {
        this.mUrl = mUrl;
    }

    public InputStream download() throws IOException {
        URL url = new URL(mUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        if(httpURLConnection.getResponseCode() == 200){
            return httpURLConnection.getInputStream();
        }
        else{
            throw new IOException("Can't download image");
        }
    }
}
