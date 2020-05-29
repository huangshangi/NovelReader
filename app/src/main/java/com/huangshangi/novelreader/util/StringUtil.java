package com.huangshangi.novelreader.util;

import com.spreada.utils.chinese.ZHConverter;

public class StringUtil {

    public static boolean isEmpty(String s){
        return s==null||s.isEmpty();
    }

    //将字体转化为繁体 简体  true->繁体 false->>简体
    public static String transformTradition(String source,boolean flag){

        if(flag)
            return ZHConverter.convert(source,ZHConverter.TRADITIONAL);
        else
            return ZHConverter.convert(source,ZHConverter.SIMPLIFIED);
    }


    //根据传入的字符串生成相应hash值
    public static String getHash(String string){
        int div=11113;
        long hashCode=0;
        for(char c:string.toCharArray())
            hashCode=((hashCode<<5)+(c-'a'))%div;

        return String.valueOf(hashCode);
    }
}
