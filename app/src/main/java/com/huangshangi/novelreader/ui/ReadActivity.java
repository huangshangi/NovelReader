package com.huangshangi.novelreader.ui;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.huangshangi.novelreader.ClickReigon;
import com.huangshangi.novelreader.Font;
import com.huangshangi.novelreader.MyApplication;
import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.ReadStyle;
import com.huangshangi.novelreader.ReadingSetting;
import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.bean.Chapter;
import com.huangshangi.novelreader.callback.ResultCallback;
import com.huangshangi.novelreader.crawler.BQG5200Crawler;
import com.huangshangi.novelreader.dao.dbservice.ChapterDBService;
import com.huangshangi.novelreader.util.DialogFactory;
import com.huangshangi.novelreader.util.StringUtil;
import com.huangshangi.novelreader.util.SysUtil;
import com.huangshangi.novelreader.webapi.CommonApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadActivity extends AppCompatActivity {



    Dialog dialog_setting;//简要设置

    Dialog dialog_setting_detail;//详细设置

    RecyclerView recyclerView_content;//内容

    ReadContentAdapter readContentAdapter;//内容适配器

    LinearLayoutManager linearLayoutManager;

    LinearLayout linearLayout;//侧滑栏linearlayout

    DrawerLayout drawerLayout;//侧滑菜单

    ListView listView;//侧滑栏章节兰

    TextView tvInterted;//正倒序

    TextView tvCatalog;//目录

    ChapterTitleChapter chapterTitleChapter;//侧滑栏适配器

    ProgressBar progressBar;

    boolean isInvertedFlag=false;//章节正序还是倒序

    Point currentPoint;

    List<Chapter> list;

    List<Chapter>invertedList;

    Book book;

    ReadingSetting setting;

    ChapterDBService chapterDBService;

    boolean isAutoScroll;

    int scrollSpeed;

    boolean shouldSmooth;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what){
                case 1:

                    init();
                    getChapterContent(book.getHistoryChpater());

                    invertedList.clear();
                    invertedList.addAll(list);
                    Collections.reverse(invertedList);
                    break;

                case 2:
                    int position=msg.arg1;

                    recyclerView_content.scrollToPosition(position);
                    if(book.getHistoryChpater()<position)
                        delayTurnToChapter(position);
                    progressBar.setVisibility(View.GONE);
                    break;
                case 3:
                    recyclerView_content.scrollBy(0,2);
                    break;
            }

            super.handleMessage(msg);
        }
    };




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getSupportActionBar()!=null){


            getSupportActionBar().hide();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        setContentView(R.layout.activity_reading);

        chapterDBService=new ChapterDBService();
        book=(Book) getIntent().getSerializableExtra("BOOK");

        list=new ArrayList<>();
        invertedList=new ArrayList<>();
        initChapters();
        //initBook();


        //适配器初始化
        readContentAdapter=new ReadContentAdapter(this,list,book);
        chapterTitleChapter=new ChapterTitleChapter(this,list);

        progressBar=findViewById(R.id.pb_loading);

        //初始化内容区域
        recyclerView_content=findViewById(R.id.rv_content);

        linearLayoutManager=new LinearLayoutManager(this);

        recyclerView_content.setLayoutManager(linearLayoutManager);

        recyclerView_content.setAdapter(readContentAdapter);//适配器为初始化



        //初始化侧滑菜单
        drawerLayout=findViewById(R.id.dl_read_activity);
        drawerLayout.setLayoutMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        listView=findViewById(R.id.lv_chapter_list);
        listView.setAdapter(chapterTitleChapter);//未初始化

        linearLayout=findViewById(R.id.ll_chapter_list_view);

        tvInterted=findViewById(R.id.tv_chapter_sort);//
        tvCatalog=findViewById(R.id.tv_book_list);

        currentPoint=new Point();

        setting=ReadingSetting.getInstance();

        isAutoScroll=setting.isAutoReading();
        scrollSpeed=setting.getAutoReadingSpeed();



        initSetting();
        initDetailSetting();

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });


        //为阅读界面设置点击监听器
        recyclerView_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        if(recyclerView_content.getScrollState()==0&&getReigon((int)event.getX(),(int)event.getY())==ClickReigon.MIDDLE)
                            dialog_setting.show();
                        break;
                }



                return false;
            }
        });

        recyclerView_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAutoScroll=false;
            }
        });
        recyclerView_content.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(linearLayoutManager!=null)
                            book.setHistoryChpater(linearLayoutManager.findLastVisibleItemPosition());
                    }
                }).start();
            }
        });


        //为侧滑栏设置监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                drawerLayout.closeDrawer(Gravity.LEFT);
                if(isInvertedFlag)
                    position=list.size()-position-1;

                getChapterContent(position);

                listView.setSelection(position);

                }


        });

        //为正倒序设置监听器
        tvInterted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInvertedFlag){
                    isInvertedFlag=false;
                    tvInterted.setText("正序");

                }else{
                    isInvertedFlag=true;
                    tvInterted.setText("倒序");
                }
                resetSortChapter();
            }
        });

        progressBar.setVisibility(View.VISIBLE);

    }


    //简要设置
    public void initSetting(){
        if(dialog_setting!=null)
            return;
        dialog_setting= DialogFactory.getReadSetting(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadActivity.this.finish();
            }
        }, null, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = linearLayoutManager.findFirstVisibleItemPosition();
                getChapterContent(current - 1 >=0 ? current-1 : 0);
                //更新保存位置

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = linearLayoutManager.findLastVisibleItemPosition();
                getChapterContent(current + 1 >= list.size() ? list.size()-1 : current + 1);
            }
        }, new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    int chapterCount=progress*list.size()/100;
                    recyclerView_content.scrollToPosition(chapterCount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_setting.dismiss();

                drawerLayout.openDrawer(Gravity.LEFT);
            }
        }, null, new DialogFactory.DayAndNightListener() {
            @Override
            public void onClick(View v) {

                setting.setDayMode(!setting.isDayMode());

                init();

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_setting.dismiss();
                dialog_setting_detail.show();
            }
        });
    }

    //详细设置
    public void initDetailSetting(){
        if(dialog_setting_detail!=null)
            return;
        dialog_setting_detail=DialogFactory.getSettingDetailDialog(this, new DialogFactory.TextSizeListener() {
            @Override
            public void onClick(View view, int textSize) {
                ReadingSetting.getInstance().setTextSize(textSize);
                initContent();//重新设置阅读界面
            }
        }, new DialogFactory.TradSimpListener() {
            @Override
            public void changeLanguage(View view, boolean isTrad) {
                setting.setTradition(isTrad);
                readContentAdapter.notifyDataSetChanged();
            }
        }, new DialogFactory.ReadStyleListener() {
            @Override
            public void onChange(View view, ReadStyle readStyle) {

                changeStyle(readStyle);
                init();

            }
        }, new DialogFactory.ScrollSpeedListener() {
            @Override
            public void changeSpeed(View view, int progress,boolean flag) {
                scrollSpeed=progress;
                isAutoScroll=flag;
                if(isAutoScroll)
                    autoScroll();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void initContent(){

        if(setting.isDayMode()){
            drawerLayout.setBackgroundResource(setting.getBgColor());
        }else{
            drawerLayout.setBackgroundResource(R.color.sys_night_bg);
        }

        if(readContentAdapter!=null){
            readContentAdapter.setList(list);
            readContentAdapter.notifyRepaint();
        }


    }

    //
    private void changeStyle(ReadStyle readStyle){
        setting.setReadStyle(readStyle);
        switch (readStyle){
            case common:
                setting.setTextColor(R.color.sys_common_word);
                setting.setBgColor(R.color.sys_common_bg);
                break;
            case leather:
                setting.setTextColor(R.color.sys_leather_word);
                setting.setBgColor(R.mipmap.theme_leather_bg);
                break;
            case protectedEye:
                setting.setBgColor(R.color.sys_protect_eye_bg);
                setting.setTextColor(R.color.sys_protect_eye_word);
                break;
            case breen:
                setting.setBgColor(R.color.sys_breen_bg);
                setting.setTextColor(R.color.sys_breen_word);
                break;
            case blueDeep:
                setting.setTextColor(R.color.sys_blue_deep_word);
                setting.setBgColor(R.color.sys_blue_deep_bg);
                break;
        }
    }


    private void init(){
        initContent();
        initChapterContent();
    }

    @SuppressLint("ResourceAsColor")
    private void initChapterContent(){

        if(setting.isDayMode()){
            linearLayout.setBackgroundResource(setting.getBgColor());
            tvCatalog.setTextColor(getResources().getColor(setting.getTextColor()));
            tvInterted.setTextColor(getResources().getColor(setting.getTextColor()));
        }else{
            linearLayout.setBackgroundResource(R.color.sys_night_bg);
            tvCatalog.setTextColor(getResources().getColor(R.color.sys_night_word));
            tvInterted.setTextColor(getResources().getColor(R.color.sys_night_word));
        }

        if(chapterTitleChapter!=null){
            resetSortChapter();
        }

        if(readContentAdapter!=null){
            readContentAdapter.setList(list);
            readContentAdapter.notifyRepaint();
        }


    }



    public void initChapters(){

        list=chapterDBService.getChaptersMenu(book.getBookId());
        if(list.size()!=0){
            Message message=new Message();
            message.what=1;
            handler.sendMessage(message);


        }else{


            CommonApi.getChapterContent(book.getBookChapterUrl(), new ResultCallback() {
                @Override
                public void onFinish(Object object, int code) {
                    String html=(String)object;
                    list= BQG5200Crawler.getInstance().getChapters(book.getBookId(),html);

                    chapterDBService.addCacheChaptersMenu(list);
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);



                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }




    }


    /**
     * 延迟跳转章节(防止跳到章节尾部)
     */
    private void delayTurnToChapter(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                    handler.sendMessage(handler.obtainMessage(2, position, 0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //获取某一章的数据
    private void getChapterContent(final int position){
        progressBar.setVisibility(View.VISIBLE);
        final Chapter chapter=list.get(position);

        if(StringUtil.isEmpty(chapter.getChapterContent())){

            CommonApi.getChapterContent(chapter.getChapterUrl(), new ResultCallback() {
                @Override
                public void onFinish(Object object, int code) {
                    final String content=BQG5200Crawler.getInstance().getContent((String)object);

                    chapter.setChapterContent(content);

                    chapterDBService.updateCacheChapter(chapter);

                    Message message=new Message();
                    message.what=2;
                    message.arg1= position;
                    handler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {

                }
            });


        }

        else{
            recyclerView_content.scrollToPosition(position);

            if(book.getHistoryChpater()<position)
                delayTurnToChapter(position);
            else
                progressBar.setVisibility(View.GONE);
        }


    }


    //判断当前点击的处于哪个区域
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private ClickReigon getReigon(int x, int y){

        //只需初始化一次 待优化
        Point point=new Point();


        getWindowManager().getDefaultDisplay().getRealSize(point);

        int width=point.x;
        int height=point.y;
        Point leftPoint=new Point(width/4,height/4);
        Point rightPoint=new Point(3*width/4,height/4);
        Point leftBottomPoint=new Point(width/4,3*height/4);
        Point rightBottomPoint=new Point(3*width/4,3*height/4);

        if(x< leftPoint.x)
            return ClickReigon.LEFT;
        else if(y<leftPoint.y)
            return ClickReigon.TOP;
        else if(y>rightBottomPoint.y)
            return ClickReigon.BOTTOM;
        else if(x<rightPoint.x)
            return ClickReigon.MIDDLE;
        else
            return ClickReigon.RIGHT;
    }

    //为正倒序重新设置chapter
    private void resetSortChapter(){
        if(isInvertedFlag)
            chapterTitleChapter.setList(invertedList);
        else
            chapterTitleChapter.setList(list);

        chapterTitleChapter.notifyDataSetChanged();
    }



    private void autoScroll(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isAutoScroll){
                    try {
                        Thread.sleep(200-scrollSpeed);
                        Log.e("speed",300-scrollSpeed+"");
                        handler.sendEmptyMessage(3);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //保存当前阅读章节

    }

    @Override
    protected void onResume() {
        //readContentAdapter.notifyRepaint();
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    Font font=(Font)data.getSerializableExtra("FONT");
                    setting.setTypeFace(font);
                    readContentAdapter.notifyRepaint(font);
                }
        }
    }

    public class TopSmoothScroller extends LinearSmoothScroller {
        TopSmoothScroller(Context context) {
            super(context);
        }
        @Override
        protected int getHorizontalSnapPreference() {
            return SNAP_TO_START;
        }
        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;  // 将子view与父view顶部对齐
        }
    }

}
