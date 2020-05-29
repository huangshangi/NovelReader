package com.huangshangi.novelreader.webapi;

import android.util.Log;

import com.huangshangi.novelreader.callback.HttpCallback;
import com.huangshangi.novelreader.callback.ResultCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.transform.Result;

public class CommonApi extends BaseApi{

    //获取某一章小说的内容信息
    public static void getChapterContent(String url, ResultCallback resultCallback){

        HttpGet(url,null,resultCallback);

    }


    //根据key获取某个列表
    public static void getBookListByKey(String url, String key, ResultCallback resultCallback)  {

        try{
            key=URLEncoder.encode(key,"gbk");
            String params="searchtype=articlename&searchkey="+key+"&action=login&submit=%26%23160%3B%CB%D1%26%23160%3B%26%23160%3B%CB%F7%26%23160%3B";

            HttpPost(url,params,resultCallback);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
