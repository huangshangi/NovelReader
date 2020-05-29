package com.huangshangi.novelreader;


//阅读时设置内容
public class ReadingSetting {

    private  static ReadingSetting readingSetting;

    //默认设置未实现
    private ReadingSetting(){
        textColor=R.color.sys_common_word;
        isDayMode=true;
        bgColor=R.color.sys_common_bg;
        textSize=20;
        brightness=50;
        typeFace=Font.默认字体;
        isAutoReading=false;
        autoReadingSpeed=50;
        isBrightnessFollowSystem=true;
        readStyle=ReadStyle.common;
        isTradition=false;
    }

    public static ReadingSetting getInstance(){
        if(readingSetting==null){
            //严谨定义未实现
            if(readingSetting==null){
                readingSetting=new ReadingSetting();
            }
        }
        return readingSetting;
    }

    private int textColor;//字体颜色

    private int bgColor;//背景颜色

    private int brightness;//屏幕亮度

    private int textSize;//字体大小

    private Font typeFace;//字体

    private boolean isAutoReading;//是否开启自动阅读

    private int autoReadingSpeed;//自动阅读速度

    private boolean isDayMode;//是否为日间模式

    private boolean isBrightnessFollowSystem;//亮度是否跟随系统

    private boolean isTradition;//是否为繁体

    private ReadStyle readStyle;//阅读模式



    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Font getTypeFace() {
        return typeFace;
    }

    public void setTypeFace(Font typeFace) {
        this.typeFace = typeFace;
    }

    public boolean isAutoReading() {
        return isAutoReading;
    }

    public void setAutoReading(boolean autoReading) {
        isAutoReading = autoReading;
    }

    public int getAutoReadingSpeed() {
        return autoReadingSpeed;
    }

    public void setAutoReadingSpeed(int autoReadingSpeed) {
        this.autoReadingSpeed = autoReadingSpeed;
    }

    public boolean isDayMode() {
        return isDayMode;
    }

    public void setDayMode(boolean dayMode) {
        isDayMode = dayMode;
    }

    public boolean isBrightnessFollowSystem() {
        return isBrightnessFollowSystem;
    }

    public void setBrightnessFollowSystem(boolean brightnessFollowSystem) {
        isBrightnessFollowSystem = brightnessFollowSystem;
    }

    public ReadStyle getReadStyle() {
        return readStyle;
    }

    public void setReadStyle(ReadStyle readStyle) {
        this.readStyle = readStyle;
    }

    public boolean isTradition() {
        return isTradition;
    }

    public void setTradition(boolean tradition) {
        isTradition = tradition;
    }
}
