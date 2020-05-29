package com.huangshangi.novelreader.callback;

public interface ResultCallback {

    public void onFinish(Object object,int code);


    public void onError(Exception e);
}
