package com.huangshangi.novelreader.ui;


import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huangshangi.novelreader.ClickReigon;
import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.ReadStyle;
import com.huangshangi.novelreader.ReadingSetting;
import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.bean.Chapter;
import com.huangshangi.novelreader.callback.ResultCallback;
import com.huangshangi.novelreader.crawler.BQG5200Crawler;
import com.huangshangi.novelreader.dao.dbservice.ChapterDBService;
import com.huangshangi.novelreader.util.DialogFactory;
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

    boolean isInvertedFlag=false;//章节正序还是倒序

    Point currentPoint;

    List<Chapter> list;

    List<Chapter>invertedList;

    Book book;

    ReadingSetting setting;

    ChapterDBService chapterDBService;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.e("这是测试","成功执行aa"+msg.what);
            switch (msg.what){
                case 1:
                    Log.e("这是测试","成功执行2");
                    init();
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
            Log.e("测试信息","ceshi");

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


        //初始化内容区域
        recyclerView_content=findViewById(R.id.rv_content);

        linearLayoutManager=new LinearLayoutManager(this);

        recyclerView_content.setLayoutManager(linearLayoutManager);

        recyclerView_content.setAdapter(readContentAdapter);//适配器为初始化
        //滑动至上次阅读位置
        //recyclerView_content.scrollToPosition(book.getHistoryChpater());

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




        initSetting();
        initDetailSetting();
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


        //为侧滑栏设置监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                drawerLayout.closeDrawer(Gravity.LEFT);
                if(!isInvertedFlag)
                    recyclerView_content.scrollToPosition(position);
                else
                    recyclerView_content.scrollToPosition(list.size()-position-1);
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


        //跳转到上一次阅读章节
       // recyclerView_content.scrollToPosition(book.getHistoryChpater());
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
                recyclerView_content.scrollToPosition(current - 1);
                //更新保存位置

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = linearLayoutManager.findLastVisibleItemPosition();
                recyclerView_content.scrollToPosition(current + 1 > list.size() ? list.size() : current + 1);
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
        }, new DialogFactory.ReadStyleListener() {
            @Override
            public void onChange(View view, ReadStyle readStyle) {

                changeStyle(readStyle);
                init();

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
                invertedList.clear();
                invertedList.addAll(list);
                Collections.reverse(invertedList);

        }else{

        }

        CommonApi.getChapterContent(book.getBookChapterUrl(), new ResultCallback() {
            @Override
            public void onFinish(Object object, int code) {
                String html=(String)object;
                list= BQG5200Crawler.getInstance().getChapters(book.getBookId(),html);

                chapterDBService.addCacheChaptersMenu(list);
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
                if(invertedList.size()==0){
                    invertedList.addAll(list);
                    Collections.reverse(invertedList);
                }


            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });



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




    private int getSystemBrightness() {
        int brightness = 0;
        try {
            brightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            Log.e("brightness",""+brightness);
        }catch (Exception e){
            e.printStackTrace();
        }
        return brightness;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //保存当前阅读章节

    }

    @Override
    protected void onResume() {
        readContentAdapter.notifyRepaint();
        super.onResume();
    }
}
