package com.huangshangi.novelreader.callback;


//进行http请求完成后 所回调的接口
public interface HttpCallback {


    public void onFinish(Object object,int code);


    public void onError(Exception e);
}
