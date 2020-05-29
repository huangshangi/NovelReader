package com.huangshangi.novelreader.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.huangshangi.novelreader.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SysUtil {


    //设置屏幕亮度
    public static void setBrightness(Activity activity,int progress){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(progress) * (1f / 100f);
        activity.getWindow().setAttributes(lp);
    }

    //获取屏幕亮度 *100
    public static int getBrightness(Activity activity){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        return (int)lp.screenBrightness*100;
    }


    //获取当前时间 2020-05-29 19:28:00
    public static String getCurrentTime(){

        Date date=new Date();

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


        return formatter.format(date);
    }


    //收起软键盘
    public static void hideKeyboard(Activity activity){
        InputMethodManager imm =  (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
                0);
    }
    }
}
