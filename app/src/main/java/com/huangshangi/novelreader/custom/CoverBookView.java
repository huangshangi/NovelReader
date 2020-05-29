package com.huangshangi.novelreader.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.huangshangi.novelreader.R;

public class CoverBookView extends View {

    //滑动模式
    enum MODE{
        LEFTTORIGHT,//从左向右滑
        RIGHTTOLEFT,//从右向左滑
        NONE;
    }

    float downX;//被按下的x坐标

    float currentX;//当前点的x坐标

    float distance=0;

    Bitmap currentBitmap,preBitmap,nextBitmap;

    Canvas currentCanvas,preCanvas,nextCanvas;

    Paint currentPaint,prePaint,nextPaint;

    int viewWidth,viewHeight;

    Scroller scroller;

    GradientDrawable shadowDrawable;

    boolean isAnim;//正在进行动画

    public CoverBookView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        viewWidth=1080;
        viewHeight=1800;
        currentBitmap= Bitmap.createBitmap(viewWidth,viewHeight, Bitmap.Config.RGB_565);
        preBitmap=Bitmap.createBitmap(viewWidth,viewHeight, Bitmap.Config.RGB_565);
        nextBitmap=Bitmap.createBitmap(viewWidth,viewHeight, Bitmap.Config.RGB_565);
        currentCanvas=new Canvas(currentBitmap);
        preCanvas=new Canvas(preBitmap);
        nextCanvas=new Canvas(nextBitmap);
        currentPaint=new Paint(Color.RED);
        prePaint=new Paint(Color.GREEN);
        nextPaint=new Paint(Color.BLUE);
        scroller=new Scroller(context,new LinearInterpolator());

        int[] mBackShadowColors = new int[] { 0x66000000,0x00000000};
        shadowDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
        shadowDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        currentX=event.getX();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                distance=currentX-downX;


                break;

            case MotionEvent.ACTION_UP:
                if(!isAnim)
                if(Math.abs(distance)<viewWidth/2.0)
                    cancelPageAnim();
                else
                    turnPageAnim();
                break;

        }

        postInvalidate();

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        preCanvas.drawColor(Color.GREEN);
        nextCanvas.drawColor(Color.RED);
        currentCanvas.drawColor(Color.BLUE);
        canvas.drawBitmap(currentBitmap,distance,0,null);
        if(distance>0)
        canvas.drawBitmap(preBitmap,distance-viewWidth,0,null);
        else
        canvas.drawBitmap(nextBitmap,viewWidth+distance,0,null);

        shadowDrawable.setBounds((int) (viewWidth+distance),0,(int) (viewWidth+30+distance),viewHeight);
        shadowDrawable.draw(canvas);
        super.onDraw(canvas);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            float x=scroller.getCurrX();
            if(x==scroller.getFinalX()){
                isAnim=false;
                //进行也的变换
                if(distance>0)
                    ;
            }

            distance=x;
            postInvalidate();

        }
    }

    private void cancelPageAnim(){
        isAnim=true;
        scroller.startScroll((int)distance,0,(int)-distance,0,400);
    }


    private void turnPageAnim(){
        isAnim=true;
        if(distance>0)
             scroller.startScroll((int)distance,0,(int)(viewWidth-distance),0,400);
        else
            scroller.startScroll((int)distance,0,(int)(-viewWidth-distance),0,400);
    }
}
