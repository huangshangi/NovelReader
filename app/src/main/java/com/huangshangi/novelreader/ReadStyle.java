package com.huangshangi.novelreader;

public enum ReadStyle {

    common(0),//普通
    leather(1),//羊皮纸
    protectedEye(2),//护眼
    breen(3),//
    blueDeep(4);//深蓝
    ReadStyle(int index) {


    }



    public static ReadStyle get(int var0) {
        return values()[var0];
    }

}
