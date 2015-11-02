package com.joedsantiago.stratpoint.androidassesment.network;

import java.util.List;

/**
 * Created by dyoed on 10/31/15.
 */
public interface Request<T extends List> {

    void get(String url, RequestCallback<T> requestCallback);

    void cancelRequest(String url);
}
