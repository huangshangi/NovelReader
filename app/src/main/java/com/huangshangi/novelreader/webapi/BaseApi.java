package com.huangshangi.novelreader.webapi;


import android.util.Log;

import com.huangshangi.novelreader.callback.ResultCallback;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

//进行底层的get post请求
public class BaseApi {


    //get方式
    public static void HttpGet(final String link, final String params, final ResultCallback callback){


        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                URL url=null;
                try {
                    url=new URL(link+(params==null?"":"?"+params));
                    connection=(HttpURLConnection)url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.connect();

                    int code=connection.getResponseCode();
                    if(code==HttpURLConnection.HTTP_OK){
                        callback.onFinish(isToString(connection.getInputStream()),code);
                    }else
                        callback.onFinish("请求失败"+code,code);

                } catch (Exception e) {
                    callback.onError(e);
                    e.printStackTrace();

                }finally {
                    if(connection!=null)
                        connection.disconnect();
                }

            }
        }).start();

    }

    //post方式
    public static void HttpPost(final String link, final String params, final ResultCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url=null;
                HttpURLConnection connection=null;

                try {
                    url=new URL(link);
                    connection=(HttpURLConnection)url.openConnection();
                    Log.e("这是结果","4005");
                    connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0");
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.connect();
                    Log.e("这是结果","4006");
                    if(params!=null||params.equals("")){
                        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"GBK"));
                        writer.write(params);
                        writer.close();
                    }
                    Log.e("这是结果","4007");
                    int code=connection.getResponseCode();
                    if(code==HttpURLConnection.HTTP_OK)
                        callback.onFinish(isToString(connection.getInputStream()),code);
                    else
                        callback.onFinish("post请求失败",code);

                    Log.e("这是结果","4008");
                }catch (Exception e){
                    e.printStackTrace();
                    callback.onError(e);

                    Log.e("这是结果","4009");
                }finally {
                    if(connection!=null)
                        connection.disconnect();
                }
            }
        }).start();
    }



    //将inputstream流转化为string
    private static String isToString(InputStream inputStream) throws Exception{

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
        StringBuilder response = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            response.append(line);
            line = reader.readLine();
        }

        return response.toString();
    }
}
