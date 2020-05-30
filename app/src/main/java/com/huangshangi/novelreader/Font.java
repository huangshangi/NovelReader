package com.huangshangi.novelreader;

import java.io.Serializable;

public enum Font implements Serializable {

    默认字体(""),
    方正楷体("fonts/fangzhengkaiti.ttf"),
    方正行楷("fonts/fangzhengxingkai.ttf"),
    经典宋体("fonts/songti.ttf"),
    迷你隶书("fonts/mini_lishu.ttf"),
    方正黄草("fonts/fangzhenghuangcao.ttf"),
    书体安景臣钢笔行书("fonts/shuti_anjingchen_gangbixingshu.ttf");


    public String path;

    Font(String path) {
        this.path = path;
    }

    public static Font get(int var0) {
        return values()[var0];
    }

}
