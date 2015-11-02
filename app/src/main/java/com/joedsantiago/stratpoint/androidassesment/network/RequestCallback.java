package com.joedsantiago.stratpoint.androidassesment.network;

/**
 * Created by dyoed on 10/31/15.
 */
public interface RequestCallback<T> {

    void onCompleted(T t);
    void onError(String errMsg);

}
