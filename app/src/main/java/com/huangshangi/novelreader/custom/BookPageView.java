package com.huangshangi.novelreader.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.ReadingSetting;
import com.huangshangi.novelreader.SlideMode;



public class BookPageView extends View {


    enum STATE{

        TURNPAGE,//当前正在进行翻页
        CANCELPAGE,//当前正在取消翻页
        NONE;
    }

    enum CURRENTPAGE{
        LASTPAGE,
        NOWPAGE,
        NEXTPAGE
    }


    //决定翻页画法的点 a->触摸点 f->翻页起始点
    private Point a,b,c,d,e,f,g,h,i,j,k,m;



    int viewWidth=1080,viewHeight=1720;

    SlideMode mode;//当前view处于的模式

    Paint backPaint;

    Path pathA,pathB,pathC,pathDefault;

    Color currentColor,backColor;


    Canvas cacheCanvas,aCanvas,bCanvas,cCanvas;

    Bitmap cacheBitmap,aBitmap,bBitmap,cBitmap;

    Scroller scroller;

    StaticLayout slCurrent,slPre,slNext;//用于换行绘制

    String content="1111",nextContent="222222",lastContennt="3333";

    STATE state;

    float startX,startY;//鼠标按下的点

    TextPaint textPaint;

    int index;//当前正在显示的章数

    int textCount;//每页可显示的文字数目

    CURRENTPAGE currentpage;

    ReadingSetting setting;

    ContentLoadListener listener;//要使用该view 必须注册

    private GradientDrawable drawableLeftTopRight;
    private GradientDrawable drawableLeftLowerRight;

    private GradientDrawable drawableRightTopRight;
    private GradientDrawable drawableRightLowerRight;
    private GradientDrawable drawableHorizontalLowerRight;

    private GradientDrawable drawableBTopRight;
    private GradientDrawable drawableBLowerRight;

    private GradientDrawable drawableCTopRight;
    private GradientDrawable drawableCLowerRight;

    float lPathAShadowDis = 0;//A区域左阴影矩形短边长度参考值
    float rPathAShadowDis = 0;//A区域右阴影矩形短边长度参考值
    private float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };

    public BookPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
        generateBitmap();
        backPaint=new Paint();
        backPaint.setColor(getResources().getColor(R.color.sys_common_bg));
        backPaint.setAntiAlias(true);


        pathA=new Path();
        pathB=new Path();
        pathC=new Path();


        pathDefault=new Path();
        scroller=new Scroller(context,new LinearInterpolator());
        setPathDefault();
        state=STATE.NONE;
        setting=ReadingSetting.getInstance();



        textPaint=new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40.0f);

        slCurrent=getStaticLayout(content);
        slPre=getStaticLayout(lastContennt);
        slNext=getStaticLayout(nextContent);
        createGradientDrawable();

        textCount=calcTextCount();

        Log.e("每页可显示的字数",""+textCount);
    }

    private int calcTextCount(){

        String string="测";
        Rect rect=new Rect();
        //通过画笔获得文字的边框
        textPaint.getTextBounds(string,0,string.length(),rect);
        Log.e("宽度",""+viewWidth/rect.width());
        Log.d("高度",viewHeight/rect.height()+"");
        return  viewWidth/rect.width()*(viewHeight/rect.height()-1);
    }

    private StaticLayout getStaticLayout(String content){
        return new StaticLayout(content,textPaint,viewWidth, Layout.Alignment.ALIGN_NORMAL,1,0,true);
    }

    //必须实现的方法 读书背景切换
    public void backChange(){

    }


    //生成三张
    private void generateBitmap(){
        content="网友编写了许多腾讯与360的小段子来调侃两家软件公司。“我们作出了一个非常艰难的决定”一举超越“我爸是李刚”成为网络最热词。 百度版：“百度作了一个艰难的决定，如果检测到用户IE里出现谷歌，将重启电脑。” 可口可乐版：“可口可乐做了个艰难的决定，如果监测到用户胃里有百事可乐，将自动释放农药和汞。” 携程网版：“携程网刚刚做了一个艰难的决定，如果发现乘客浏览 旅盟网，携程网将让飞机停飞” 联通版：“联通作了个艰难的决定，如果方圆百米内检测到有移动用户 将使这些用户不间断自动拨打110。” 诺基亚版：“NOKIA做出艰难决定,如果检测到家里有MOTO手机,NOKIA将自动引暴。” 迅雷版：“迅雷作了一个艰难的决定，如果检测到用户电脑曾安装过快车，将拒绝下载教育片。” 电信版：“电信作了个艰难的决定，如果监测到用户小区里有使用网通，将自动短路。” 春歌版：“春哥作了个艰难的决定，如果检测到女人，将使其自动升级为纯爷们。”";

        content+=content+content+content+content+content+content+content+content+content;

        content=content.substring(0,200);
        nextContent="  百度版：“百度作了一个艰难的决定，如果检测到用户IE里出现谷歌，将重启电脑。” 可口可乐版：“可口可乐做了个艰难的决定，如果监测到用户胃里有百事可乐，将自动释放农药和汞。” 携程网版：“携程网刚刚做了一个艰难的决定，如果发现乘客浏览 旅盟网，携程网将让飞机停飞” 联通版：“联通作了个艰难的决定，如果方圆百米内检测到有移动用户 将使这些用户不间断自动拨打110。”";

        nextContent+=nextContent+nextContent+nextContent+nextContent+nextContent+nextContent+nextContent+nextContent+nextContent;

        lastContennt="是腾迅QQ发表的，据称他们做了一个艰难的决定，要跟360你死我活。 于是，如果你有QQ，登录上去，就会通知你接收一封信，信上告诉你，如果你的电脑装有360杀毒软件，如果你不卸载的话，电脑重启后你就无法登陆QQ了。你也要做一个艰难的决定，是保留QQ，还是保留360.";

        lastContennt+=lastContennt+lastContennt+lastContennt+lastContennt+lastContennt+lastContennt+lastContennt+lastContennt+lastContennt;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(state!=STATE.NONE)
            return super.onTouchEvent(event);
        float x=event.getX();
        float y=event.getY();
        a.x=x;
        a.y=y;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mode=getMode(x,y);
                calcPointsPosition();
                startX=x;
                startY=y;
                break;

            case MotionEvent.ACTION_MOVE:
                if(mode==SlideMode.NONE)
                    break;
                calcPointsPosition();
                if(c.x>viewWidth||c.x<0)
                    updateAPointPosition();

                    calcPointsPosition();

                    break;
            case MotionEvent.ACTION_UP:
                if(mode==SlideMode.NONE)
                    break;

                if((Math.abs(y-startY)>viewHeight/2.0)||(Math.abs(x-startX)>viewWidth/2.0))
                        turnPageAnim();
                    else
                        cancelPageAnim();

                break;
        }

        postInvalidate();
        return super.onTouchEvent(event);
    }

    private void createGradientDrawable(){
        int deepColor = 0x33333333;
        int lightColor = 0x01333333;
        int[] gradientColors = new int[]{lightColor,deepColor};//渐变颜色数组
        drawableLeftTopRight = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        drawableLeftTopRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawableLeftLowerRight = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
        drawableLeftLowerRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x22333333;
        lightColor = 0x01333333;
        gradientColors =  new int[]{deepColor,lightColor,lightColor};
        drawableRightTopRight = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors);
        drawableRightTopRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawableRightLowerRight = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradientColors);
        drawableRightLowerRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x44333333;
        lightColor = 0x01333333;
        gradientColors = new int[]{lightColor,deepColor};//渐变颜色数组
        drawableHorizontalLowerRight = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);;
        drawableHorizontalLowerRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x55111111;
        lightColor = 0x00111111;
        gradientColors = new int[] {deepColor,lightColor};//渐变颜色数组
        drawableBTopRight =new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,gradientColors);
        drawableBTopRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);//线性渐变
        drawableBLowerRight =new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,gradientColors);
        drawableBLowerRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        deepColor = 0x55333333;
        lightColor = 0x00333333;
        gradientColors = new int[]{lightColor,deepColor};//渐变颜色数组
        drawableCTopRight = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        drawableCTopRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawableCLowerRight = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
        drawableCLowerRight.setGradientType(GradientDrawable.LINEAR_GRADIENT);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cacheBitmap=Bitmap.createBitmap(viewWidth,viewHeight, Bitmap.Config.ARGB_8888);
        cacheCanvas=new Canvas(cacheBitmap);
        canvas.drawColor(getResources().getColor(R.color.sys_common_bg));
        if(mode==SlideMode.NONE&&state==STATE.NONE){

            setPathDefault();
            drawPathA();
        }else{
            updatePath();
            drawPathA();
            drawPathC();
            drawPathB();

        }


        canvas.drawBitmap(cacheBitmap,0,0,null);

    }


    private void drawPathA(){


        aCanvas.drawPath(pathA,backPaint);
        //aCanvas.drawText(content,viewWidth-300, viewHeight-300,textPaint);

        slCurrent.draw(aCanvas);
        cacheCanvas.save();

        cacheCanvas.clipPath(pathA, Region.Op.INTERSECT);//对绘制内容进行裁剪，取和A区域的交集
        cacheCanvas.drawBitmap(aBitmap, 0, 0, null);

        if(mode==SlideMode.LEFTTORIGHT||mode==SlideMode.RIGHTTOLEFT){
            drawPathAHorizontalShadow();
        }else{
            drawPathALeftShadow();
            drawPathARightShadow();
        }

        cacheCanvas.restore();

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void drawPathB(){



        bCanvas.drawPath(pathB,backPaint);
        //待优化
        slNext.draw(bCanvas);



        cacheCanvas.save();


        pathA.op(pathC, Path.Op.UNION);
        pathA.op(pathB, Path.Op.REVERSE_DIFFERENCE);


        cacheCanvas.clipPath(pathA);

        cacheCanvas.drawBitmap(bBitmap, 0, 0, null);


        drawPathBShadow();

        cacheCanvas.restore();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void drawPathC(){


        cCanvas.drawPath(pathB,backPaint);//绘制一个背景，path用B
        slCurrent.draw(cCanvas);

        cacheCanvas.save();
        pathA.op(pathC,Path.Op.UNION);
        pathA.op(pathA,Path.Op.INTERSECT);
        cacheCanvas.clipPath(pathA);

        float eh = (float) Math.hypot(f.x - e.x,h.y - f.y);
        float sin0 = (f.x - e.x) / eh;
        float cos0 = (h.y - f.y) / eh;
        //设置翻转和旋转矩阵
        float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };
        mMatrixArray[0] = -(1-2 * sin0 * sin0);
        mMatrixArray[1] = 2 * sin0 * cos0;
        mMatrixArray[3] = 2 * sin0 * cos0;
        mMatrixArray[4] = 1 - 2 * sin0 * sin0;

        Matrix mMatrix = new Matrix();
        mMatrix.reset();
        mMatrix.setValues(mMatrixArray);//翻转和旋转
        mMatrix.preTranslate(-e.x, -e.y);//沿当前XY轴负方向位移得到 矩形A₃B₃C₃D₃
        mMatrix.postTranslate(e.x, e.y);//沿原XY轴方向位移得到 矩形A4 B4 C4 D4

        cacheCanvas.drawBitmap(cBitmap, mMatrix, null);

        drawPathCShadow();

        cacheCanvas.restore();
    }



    //初始化点
    private void init(){
        a=new Point();
        b=new Point();
        c=new Point();
        d=new Point();
        e=new Point();
        f=new Point();
        g=new Point();
        h=new Point();
        i=new Point();
        j=new Point();
        k=new Point();
        m=new Point();

        aBitmap=Bitmap.createBitmap(viewWidth,viewHeight, Bitmap.Config.RGB_565);
        aCanvas=new Canvas(aBitmap);
        cBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
        cCanvas = new Canvas(cBitmap);
        bBitmap=Bitmap.createBitmap(viewWidth,viewHeight, Bitmap.Config.RGB_565);
        bCanvas=new Canvas(bBitmap);
    }


    //根据被点击的位置获取相应的模式
    private SlideMode getMode(float x,float y){

        if(x<viewWidth/2.0&&y<viewHeight/3.0)
            return SlideMode.TOPFROMLEFT;
        else if(y<viewHeight/3.0)
            return SlideMode.TOPFROMRIGHT;
        else if(x<viewWidth/4.0&&y<2*viewHeight/3.0)
            return SlideMode.LEFTTORIGHT;
        else if(x<2*viewWidth/3.0&&y<2*viewHeight/3.0)
            return SlideMode.NONE;
        else if(y<2*viewHeight/3.0)
            return SlideMode.RIGHTTOLEFT;
        else if(x<viewWidth/2.0)
            return SlideMode.BOTTOMFROMLEFT;
        else
            return SlideMode.BOTTOMFROMRIGHT;
    }

    private void calcPointsPosition(){
        updateInitPointByMode();

        g.x=(a.x+f.x)/2;
        g.y=(f.y+a.y)/2;

        m.x=g.x;
        m.y=f.y;

        if(mode==SlideMode.LEFTTORIGHT||mode==SlideMode.BOTTOMFROMLEFT||mode==SlideMode.TOPFROMLEFT)
            calcPointsPositionLeft();
        else
            calcPointsPositionRight();

        b=getIntersectionPostion(a,e,c,j);
        k=getIntersectionPostion(c,j,a,h);

        d.x=(c.x+b.x)/4+e.x/2;
        d.y=(c.y+b.y)/4+e.y/2;

        i.x=(k.x+j.x)/4+h.x/2;
        i.y=(k.y+j.y)/4+h.y/2;
    }


    private void calcPointsPositionLeft(){
        e.x=g.x+(f.y-g.y)*(f.y-g.y)/(g.x-f.x);
        e.y=f.y;

        h.x=f.x;
        h.y=g.y+(g.x-f.x)*(g.x-f.x)/(g.y-f.y);

        c.x=f.x+3*(e.x-f.x)/2;
        c.y=f.y;

        j.x=f.x;
        j.y=f.y+3*(h.y-f.y)/2;


    }

    private void calcPointsPositionRight(){



        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        h.x = f.x;
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y);

        c.x=(3*e.x-f.x)/2;
        c.y=f.y;

        j.x=f.x;
        j.y=(3*h.y-f.y)/2;



    }

    //获取两点间距离
    private float getDistance(Point a,Point b){


        return (float) Math.pow(Math.pow(a.x-b.x,2)+Math.pow(a.y-b.y,2),0.5);
    }


    //得到两条直线相交的点
    private Point getIntersectionPostion(Point a,Point b,Point c,Point d){
        float x1=a.x,x2=b.x,x3=c.x,x4=d.x;
        float y1=a.y,y2=b.y,y3=c.y,y4=d.y;

        float pointX =((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
        float pointY =((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));

        return new Point(pointX,pointY);
    }

    //判断c,j两点是否移出屏幕
    private boolean overflowScreen(){
        Point e=new Point();
        Point g=new Point();
        Point m=new Point();
        Point h=new Point();
        g.x=(a.x+f.x)/2;
        g.y=(a.y+f.y)/2;

        m.x=g.x;
        m.y=f.y;

        e.x=f.x-(float) Math.pow(getDistance(g,f),2)/getDistance(m,f);
        e.y=f.y;

        float cx=(3*e.x-f.x)/2;

        h.x=f.x;
        h.y=f.y-getDistance(g,f)*getDistance(e,f)/getDistance(e,g);

        float jy=h.y-getDistance(h,f)/2;

        return cx<=0;
    }

    private void updateAPointPosition(){

            float w0 = Math.max(viewWidth-c.x,c.x);
            float w1 = Math.abs(f.x - a.x);
            float w2 = viewWidth * w1 / w0;
            a.x = Math.abs(f.x - w2);
            float h1 = Math.abs(f.y - a.y);
            float h2 = w2 * h1 / w1;
            a.y = Math.abs(f.y - h2);

    }

    //当c移出屏幕时 c<0 重新计算a点坐标
    public void updateAPointPositionR(){

        float w0 = viewWidth - c.x;

        float w1 = Math.abs(f.x - a.x);
        float w2 = viewWidth * w1 / w0;
        a.x = Math.abs(f.x - w2);

        float h1 = Math.abs(f.y - a.y);
        float h2 = w2 * h1 / w1;
        a.y = Math.abs(f.y - h2);

    }

    private void updateAPointPositionLeft(){
        float w0=c.x;

        float w1=a.x;
        float w2=viewWidth*w1/w0;
        a.x=w2;

        float h1=a.y;
        float h2=w2*h1/w1;
        a.y=h2;
    }

    private void updateInitPointByMode(){
        switch (mode){
            case BOTTOMFROMLEFT:
                f.x=0;
                f.y=viewHeight;
                break;
            case TOPFROMLEFT:
                f.x=0;
                f.y=0;
                break;
            case LEFTTORIGHT:
                a.y=viewHeight-2;
                f.x=0;
                f.y=viewHeight;
                break;
            case RIGHTTOLEFT:
                a.y=viewHeight-2;
                f.x=viewWidth;
                f.y=viewHeight;
                break;

            case TOPFROMRIGHT:
                f.x=viewWidth;
                f.y=0;
                break;
            case BOTTOMFROMRIGHT:
                f.x=viewWidth;
                f.y=viewHeight;
                break;
            case NONE:

                break;

        }
    }

    //设置默认path
    private void setPathDefault(){
        pathA.reset();
        pathA.lineTo(0,viewHeight);
        pathA.lineTo(viewWidth,viewHeight);
        pathA.lineTo(viewWidth,0);
        pathA.close();
    }

    private void updatePath(){
        pathA.reset();
        pathB.reset();
        pathC.reset();

        if(mode==SlideMode.BOTTOMFROMRIGHT||mode==SlideMode.RIGHTTOLEFT){
            pathA.lineTo(0,viewHeight);
            pathA.lineTo(c.x,c.y);
            pathA.quadTo(e.x,e.y,b.x,b.y);
            pathA.lineTo(a.x,a.y);
            pathA.lineTo(k.x,k.y);
            pathA.quadTo(h.x,h.y,j.x,j.y);
            pathA.lineTo(viewWidth,0);
            pathA.close();
        }else if(mode==SlideMode.TOPFROMRIGHT){
            pathA.lineTo(c.x,c.y);
            pathA.quadTo(e.x,e.y,b.x,b.y);
            pathA.lineTo(a.x,a.y);
            pathA.lineTo(k.x,k.y);
            pathA.quadTo(h.x,h.y,j.x,j.y);
            pathA.lineTo(viewWidth,viewHeight);
            pathA.lineTo(0, viewHeight);
            pathA.close();
        }else if(mode==SlideMode.BOTTOMFROMLEFT||mode==SlideMode.LEFTTORIGHT){

            pathA.moveTo(viewWidth,viewHeight);
            pathA.lineTo(c.x,c.y);
            pathA.quadTo(e.x,e.y,b.x,b.y);
            pathA.lineTo(a.x,a.y);
            pathA.lineTo(k.x,k.y);
            pathA.quadTo(h.x,h.y,j.x,j.y);
            pathA.lineTo(0,0);
            pathA.lineTo(viewWidth,0);
            pathA.close();
        }else{
            pathA.moveTo(viewWidth,0);
            pathA.lineTo(c.x,c.y);
            pathA.quadTo(e.x,e.y,b.x,b.y);
            pathA.lineTo(a.x,a.y);
            pathA.lineTo(k.x,k.y);
            pathA.quadTo(h.x,h.y,j.x,j.y);
            pathA.lineTo(0,viewHeight);
            pathA.lineTo(viewWidth,viewHeight);

            pathA.close();
        }




        pathB.lineTo(0,viewHeight);
        pathB.lineTo(viewWidth,viewHeight);
        pathB.lineTo(viewWidth,0);
        pathB.close();

        pathC.moveTo(d.x,d.y);
        pathC.lineTo(b.x,b.y);
        pathC.lineTo(a.x,a.y);
        pathC.lineTo(k.x,k.y);
        pathC.lineTo(i.x,i.y);
        pathC.close();
    }

    //更新左边path位置
    private void updatePatheft(){
        pathA.reset();
        pathB.reset();
        pathC.reset();

        pathA.moveTo(viewWidth,0);
        pathA.lineTo(c.x,c.y);
        pathA.quadTo(e.x,e.y,b.x,b.y);
        pathA.lineTo(a.x,a.y);
        pathA.lineTo(k.x,k.y);
        pathA.quadTo(h.x,h.y,j.x,j.y);
        pathA.lineTo(0,viewHeight);
        pathA.lineTo(viewWidth,viewHeight);
        pathA.close();

        pathB.lineTo(0,viewHeight);
        pathB.lineTo(viewWidth,viewHeight);
        pathB.lineTo(viewWidth,0);
        pathB.close();

        pathC.moveTo(d.x,d.y);
        pathC.lineTo(b.x,b.y);
        pathC.lineTo(a.x,a.y);
        pathC.lineTo(k.x,k.y);
        pathC.lineTo(i.x,i.y);
        pathC.close();
    }

    //更新path的位置
    private void updatePathR(){

        pathA.reset();
        pathB.reset();
        pathC.reset();




        if(mode==SlideMode.TOPFROMRIGHT){
            pathA.lineTo(c.x,c.y);
            pathA.quadTo(e.x,e.y,b.x,b.y);
            pathA.lineTo(a.x,a.y);
            pathA.lineTo(k.x,k.y);
            pathA.quadTo(h.x,h.y,j.x,j.y);
            pathA.lineTo(viewWidth,viewHeight);
            pathA.lineTo(0, viewHeight);
            pathA.close();

        }else{
            pathA.lineTo(0,viewHeight);
            pathA.lineTo(c.x,c.y);
            pathA.quadTo(e.x,e.y,b.x,b.y);
            pathA.lineTo(a.x,a.y);
            pathA.lineTo(k.x,k.y);
            pathA.quadTo(h.x,h.y,j.x,j.y);
            pathA.lineTo(viewWidth,0);
            pathA.close();
        }


        pathB.lineTo(0,viewHeight);
        pathB.lineTo(viewWidth,viewHeight);
        pathB.lineTo(viewWidth,0);
        pathB.close();

        pathC.moveTo(d.x,d.y);
        pathC.lineTo(b.x,b.y);
        pathC.lineTo(a.x,a.y);
        pathC.lineTo(k.x,k.y);
        pathC.lineTo(i.x,i.y);
        pathC.close();

    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            float x=scroller.getCurrX();
            float y=scroller.getCurrY();

            if(x==scroller.getFinalX()&&y==scroller.getFinalY()){
                mode=SlideMode.NONE;
                state=STATE.NONE;
                //向后翻页
                if(currentpage==CURRENTPAGE.NEXTPAGE){
                    slPre=slCurrent;
                    slCurrent=slNext;
                    //请求下一页

                }else if(currentpage==CURRENTPAGE.LASTPAGE){
                    slNext=slCurrent;
                    slCurrent=slPre;
                    //请求下一页
                }
            }
            else if(state==STATE.TURNPAGE){
                a.x=x;
                a.y=y;
                calcPointsPosition();

            }else{
                a.x=x;
                a.y=y;
                calcPointsPosition();
                if(c.x<0||c.x>viewWidth)
                    updateAPointPosition();
                calcPointsPosition();
            }
            postInvalidate();
        }

        super.computeScroll();
    }


    //进行翻页动画
    private void turnPageAnim(){
        state=STATE.TURNPAGE;
        int dx,dy;

        if(mode==SlideMode.RIGHTTOLEFT||mode==SlideMode.BOTTOMFROMRIGHT){
            dx = (int) (a.x-2*viewWidth+1);
            dy = (int) (viewHeight-1-a.y);
            currentpage=CURRENTPAGE.NEXTPAGE;
        }else if(mode==SlideMode.TOPFROMRIGHT){
            dx = (int) (a.x-2*viewWidth+1);
            dy = (int) (1-a.y);
            currentpage=CURRENTPAGE.NEXTPAGE;
        }else if(mode==SlideMode.BOTTOMFROMLEFT){
            dx=(int)(2*viewWidth-1-a.x);
            dy=(int)(viewHeight-a.y-1);
            currentpage=CURRENTPAGE.LASTPAGE;
        }else{
            currentpage=CURRENTPAGE.LASTPAGE;
            dx=(int)(2*viewWidth-1-a.x);
            dy = (int) (1-a.y);
        }

        scroller.startScroll((int) a.x, (int) a.y, dx, dy, 400);
    }

    //取消翻页动画
    private void cancelPageAnim(){
        state=STATE.CANCELPAGE;
        int dx,dy;
        if(mode==SlideMode.RIGHTTOLEFT||mode==SlideMode.BOTTOMFROMRIGHT){
            dx = (int) (viewWidth-1-a.x);
            dy = (int) (viewHeight-1-a.y);
        }else if(mode==SlideMode.TOPFROMRIGHT){
            dx = (int) (viewWidth-1-a.x);
            dy = (int) (1-a.y);
        }else if(mode==SlideMode.BOTTOMFROMLEFT){
            dx=(int)(1-a.x);
            dy=(int)(viewHeight-a.y-1);
        }else{
            dx=(int)(1-a.x);
            dy=(int)(a.y-viewHeight+1);
        }

        scroller.startScroll((int) a.x, (int) a.y, dx, dy, 200);
    }


    /**
     * 绘制A区域左阴影
     *
     */
    private void drawPathALeftShadow(){
        cacheCanvas.restore();
        cacheCanvas.save();

        int left;
        int right;
        int top = (int) e.y;
        int bottom = (int) (e.y+viewHeight);

        GradientDrawable gradientDrawable;
        if (mode==SlideMode.BOTTOMFROMLEFT||mode==SlideMode.TOPFROMRIGHT||mode==SlideMode.LEFTTORIGHT) {
            gradientDrawable = drawableLeftTopRight;
            left = (int) (e.x - lPathAShadowDis /2);
            right = (int) (e.x);
        } else {
            gradientDrawable = drawableLeftLowerRight;
            left = (int) (e.x);
            right = (int) (e.x + lPathAShadowDis /2);
        }

        Path mPath = new Path();
        mPath.moveTo(a.x- Math.max(rPathAShadowDis, lPathAShadowDis) /2,a.y);
        mPath.lineTo(d.x,d.y);
        mPath.lineTo(e.x,e.y);
        mPath.lineTo(a.x,a.y);
        mPath.close();
        cacheCanvas.clipPath(pathA);
        cacheCanvas.clipPath(mPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(e.x-a.x, a.y-e.y));
        cacheCanvas.rotate(mDegrees, e.x, e.y);

        gradientDrawable.setBounds(left,top,right,bottom);
        gradientDrawable.draw(cacheCanvas);
    }


    /**
     * 绘制A区域右阴影
     *
     */
    private void drawPathARightShadow(){
        cacheCanvas.restore();
        cacheCanvas.save();

        float viewDiagonalLength = (float) Math.hypot(viewWidth, viewHeight);//view对角线长度
        int left = (int) h.x;
        int right = (int) (h.x + viewDiagonalLength*10);//需要足够长的长度
        int top;
        int bottom;

        GradientDrawable gradientDrawable;
        if (mode==SlideMode.BOTTOMFROMLEFT||mode==SlideMode.TOPFROMRIGHT||mode==SlideMode.LEFTTORIGHT) {
            gradientDrawable = drawableRightTopRight;
            top = (int) (h.y- rPathAShadowDis /2);
            bottom = (int) h.y;
        } else {
            gradientDrawable = drawableRightLowerRight;
            top = (int) h.y;
            bottom = (int) (h.y+ rPathAShadowDis /2);
        }
        gradientDrawable.setBounds(left,top,right,bottom);

        Path mPath = new Path();
        mPath.moveTo(a.x- Math.max(rPathAShadowDis, lPathAShadowDis) /2,a.y);
//        mPath.lineTo(i.x,i.y);
        mPath.lineTo(h.x,h.y);
        mPath.lineTo(a.x,a.y);
        mPath.close();
        cacheCanvas.clipPath(pathA);
        cacheCanvas.clipPath(mPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(a.y-h.y, a.x-h.x));
        cacheCanvas.rotate(mDegrees, h.x, h.y);
        gradientDrawable.draw(cacheCanvas);
    }



    /**
     * 绘制A区域水平翻页阴影
     *
     */
    private void drawPathAHorizontalShadow(){
        cacheCanvas.restore();
        cacheCanvas.save();
        cacheCanvas.clipPath(pathA, Region.Op.INTERSECT);

        int maxShadowWidth = 30;//阴影矩形最大的宽度
        int left = (int) (a.x - Math.min(maxShadowWidth,(rPathAShadowDis/2)));
        int right = (int) (a.x);
        int top = 0;
        int bottom = viewHeight;
        GradientDrawable gradientDrawable = drawableHorizontalLowerRight;
        gradientDrawable.setBounds(left,top,right,bottom);

        float mDegrees = (float) Math.toDegrees(Math.atan2(f.x-a.x,f.y-h.y));
        cacheCanvas.rotate(mDegrees, a.x, a.y);
        gradientDrawable.draw(cacheCanvas);
    }


    /*
        绘制B区域阴影
     */

    private void drawPathBShadow(){

        int deepColor = 0xff111111;//为了让效果更明显使用此颜色代码，具体可根据实际情况调整

        int lightColor = 0x00111111;
        int[] gradientColors = new int[] {deepColor,lightColor};//渐变颜色数组

        int deepOffset = 0;//深色端的偏移值
        int lightOffset = 0;//浅色端的偏移值
        float aTof =(float) Math.hypot((a.x - f.x),(a.y - f.y));//a到f的距离
        float viewDiagonalLength = (float) Math.hypot(viewWidth, viewHeight);//对角线长度

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + c.y);
        GradientDrawable gradientDrawable;
        if(mode==SlideMode.BOTTOMFROMLEFT||mode==SlideMode.TOPFROMRIGHT||mode==SlideMode.LEFTTORIGHT){//f点在右上角
            //从左向右线性渐变
            gradientDrawable =new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);//线性渐变

            left = (int) (c.x - deepOffset);//c点位于左上角
            right = (int) (c.x + aTof/4 + lightOffset);
        }else {
            //从右向左线性渐变
            gradientDrawable =new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (c.x - aTof/4 - lightOffset);//c点位于左下角
            right = (int) (c.x + deepOffset);
        }
        gradientDrawable.setBounds(left,top,right,bottom);//设置阴影矩形

        float rotateDegrees = (float) Math.toDegrees(Math.atan2(e.x- f.x, h.y - f.y));//旋转角度
        cacheCanvas.rotate(rotateDegrees, c.x, c.y);//以c为中心点旋转
        gradientDrawable.draw(cacheCanvas);
    }

    /**
     * 绘制C区域阴影，阴影左浅右深
     *
     */
    private void drawPathCShadow(){
        int deepOffset = 1;//深色端的偏移值
        int lightOffset = -30;//浅色端的偏移值
        float viewDiagonalLength = (float) Math.hypot(viewWidth, viewHeight);//view对角线长度
        int midpoint_ce = (int) (c.x + e.x) / 2;//ce中点
        int midpoint_jh = (int) (j.y + h.y) / 2;//jh中点
        float minDisToControlPoint = Math.min(Math.abs(midpoint_ce - e.x), Math.abs(midpoint_jh - h.y));//中点到控制点的最小值

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + c.y);
        GradientDrawable gradientDrawable;
        if (mode==SlideMode.BOTTOMFROMLEFT||mode==SlideMode.TOPFROMRIGHT||mode==SlideMode.LEFTTORIGHT) {
            gradientDrawable = drawableCTopRight;
            left = (int) (c.x - lightOffset);
            right = (int) (c.x + minDisToControlPoint + deepOffset);
        } else {
            gradientDrawable = drawableCLowerRight;
            left = (int) (c.x - minDisToControlPoint - deepOffset);
            right = (int) (c.x + lightOffset);
        }
        gradientDrawable.setBounds(left,top,right,bottom);

        float mDegrees = (float) Math.toDegrees(Math.atan2(e.x- f.x, h.y - f.y));
        cacheCanvas.rotate(mDegrees, c.x, c.y);
        gradientDrawable.draw(cacheCanvas);
    }

    class Point{

        float x;
        float y;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }


    interface ContentLoadListener{

        //是否有下一页
        boolean getNext();

        //是否上一页
        boolean getPre();


    }
}
