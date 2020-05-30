package com.huangshangi.novelreader.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huangshangi.novelreader.DayStyle;
import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.ReadStyle;
import com.huangshangi.novelreader.ReadingSetting;
import com.huangshangi.novelreader.ui.FontsActivity;
import com.huangshangi.novelreader.ui.ReadActivity;

import java.util.ArrayList;
import java.util.List;

public class DialogFactory {

    //当前被选择的格式
    static ImageView lastReadStyle=null;

    public static Dialog getReadSetting(Context context, View.OnClickListener backListener,
                                        View.OnClickListener voiceReadListener, View.OnClickListener moreListener,
                                        View.OnClickListener lastChapterListener, View.OnClickListener nextChapterListener, SeekBar.OnSeekBarChangeListener progressListener,
                                        View.OnClickListener catalogListener, View.OnClickListener cacheListener,
                                        final DayAndNightListener dayAndNirhtListener, View.OnClickListener settingDetailListener){

        final Dialog dialog=new Dialog(context, R.style.setting_dialog);
        View view= LayoutInflater.from(context).inflate(R.layout.read_setting,null);
        dialog.setContentView(view);
        final ReadingSetting setting=ReadingSetting.getInstance();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });

        LinearLayout llBack=view.findViewById(R.id.title_back);//返回按钮
        ImageView ivVoiceRead=view.findViewById(R.id.title_voice_read);//voice读书
        ImageView ivMore=view.findViewById(R.id.title_more);//更多
        TextView tvLastChapter=view.findViewById(R.id.tv_last_chapter);//上一章
        TextView tvNextChapter=view.findViewById(R.id.tv_next_chapter);//下一章
        SeekBar sbProgress=view.findViewById(R.id.read_chapter_progress);//读书进度
        LinearLayout llCatalog=view.findViewById(R.id.ll_chapter_list);//目录
        LinearLayout llCache=view.findViewById(R.id.ll_download_cache);//缓存‘
        LinearLayout llDayAndNight=view.findViewById(R.id.ll_night_and_day);//夜间 日间 模式
        LinearLayout llSettingDetail=view.findViewById(R.id.ll_setting);//详细设置

        final ImageView ivDayAndNight=view.findViewById(R.id.iv_night_and_day);
        final TextView tvDayAndNight=view.findViewById(R.id.tv_night_and_day);



        llBack.setOnClickListener(backListener);
        ivVoiceRead.setOnClickListener(voiceReadListener);
        ivMore.setOnClickListener(moreListener);
        tvLastChapter.setOnClickListener(lastChapterListener);
        tvNextChapter.setOnClickListener(nextChapterListener);
        sbProgress.setOnSeekBarChangeListener(progressListener);
        llCatalog.setOnClickListener(catalogListener);
        llCache.setOnClickListener(cacheListener);
        llSettingDetail.setOnClickListener(settingDetailListener);
        llDayAndNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dayAndNirhtListener!=null)
                    dayAndNirhtListener.onClick(v);
                if(setting.isDayMode()){
                    ivDayAndNight.setImageResource(R.mipmap.ao);
                    tvDayAndNight.setText("日间");
                }else{
                    ivDayAndNight.setImageResource(R.mipmap.z4);
                    tvDayAndNight.setText("夜间");
                }
            }
        });


        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;

    }




    public static Dialog getSettingDetailDialog(final Activity context, final TextSizeListener textSizeListener,
                                                final TradSimpListener tradSimpListener,
                                                final ReadStyleListener readStyleListener, final ScrollSpeedListener scrollSpeedListener
                                                ){

        final Dialog dialog=new Dialog(context,R.style.setting_dialog);
        final View view=LayoutInflater.from(context).inflate(R.layout.read_detail_setting,null);
        final List<ImageView>readstyleList=new ArrayList<>();
        final ReadingSetting setting=ReadingSetting.getInstance();
        dialog.setContentView(view);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });

        SeekBar sbBrightness=view.findViewById(R.id.brightness_progress);
        final TextView tvSystemBrightness=view.findViewById(R.id.system_brightness);
        TextView tvTextSizeLess=view.findViewById(R.id.text_size_less);
        TextView tvTextSizeLarger=view.findViewById(R.id.text_size_larger);
        final TextView tvTextSize=view.findViewById(R.id.text_size);
        final TextView tvTradAndSimp=view.findViewById(R.id.simplified_and_traditional);
        TextView tvTextFont=view.findViewById(R.id.text_font);
        final SeekBar seekBar=view.findViewById(R.id.auto_scroll_progress);
        final TextView tvAutoScroll=view.findViewById(R.id.auto_scroll);

        //阅读格式
        final ImageView ivReadStyleCommon=view.findViewById(R.id.iv_common_style);
        final ImageView ivReadStyleLeather=view.findViewById(R.id.iv_leather_style);
        final ImageView ivReadStyleProtect=view.findViewById(R.id.iv_protect_eye_style);
        final ImageView ivReadStyleBreen=view.findViewById(R.id.iv_breen_style);
        final ImageView ivReadStyleBlue=view.findViewById(R.id.iv_blue_deep_style);
        final ImageView ivReadStyleNext=view.findViewById(R.id.tv_more_text_bg);
        readstyleList.add(ivReadStyleCommon);
        readstyleList.add(ivReadStyleLeather);
        readstyleList.add(ivReadStyleProtect);
        readstyleList.add(ivReadStyleBreen);
        readstyleList.add(ivReadStyleBlue);

        setReadStyle(readstyleList.get(ReadStyle.indexOf(setting.getReadStyle())));

        seekBar.setProgress(setting.getAutoReadingSpeed());
        tvAutoScroll.setSelected(setting.isAutoReading());

        sbBrightness.setProgress(SysUtil.getBrightness(context));

        if(setting.isTradition())
            tvTradAndSimp.setText("繁");
        else
            tvTradAndSimp.setText("简");

        tvAutoScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.setAutoReading(!setting.isAutoReading());
                tvAutoScroll.setSelected(setting.isAutoReading());
                if (scrollSpeedListener!=null)
                    scrollSpeedListener.changeSpeed(v,seekBar.getProgress(),setting.isAutoReading());
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (scrollSpeedListener!=null)
                    scrollSpeedListener.changeSpeed(seekBar,progress,setting.isAutoReading());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSystemBrightness.setSelected(false);
                setting.setBrightnessFollowSystem(false);
                SysUtil.setBrightness(context,progress);
                setting.setBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvSystemBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSystemBrightness.setSelected(!tvSystemBrightness.isSelected());
                setting.setBrightnessFollowSystem(false);
                SysUtil.setBrightness(context,SysUtil.getBrightness(context));
                setting.setBrightness(SysUtil.getBrightness(context));
            }
        });
        tvTextSizeLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(textSizeListener!=null)
                    textSizeListener.onClick(v,setting.getTextSize()-1);


                tvTextSize.setText(String.valueOf(setting.getTextSize()));
            }
        });

        tvTextSizeLarger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textSizeListener!=null)
                    textSizeListener.onClick(v,setting.getTextSize()+1);

                tvTextSize.setText(String.valueOf(setting.getTextSize()));
            }
        });


        //阅读格式系列
        ivReadStyleCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReadStyle(ivReadStyleCommon);
                if(readStyleListener!=null)
                    readStyleListener.onChange(v,ReadStyle.common);
            }
        });

        ivReadStyleLeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReadStyle(ivReadStyleLeather);
                if(readStyleListener!=null)
                    readStyleListener.onChange(v,ReadStyle.leather);
            }
        });

        ivReadStyleProtect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReadStyle(ivReadStyleProtect);
                if(readStyleListener!=null)
                    readStyleListener.onChange(v,ReadStyle.protectedEye);
            }
        });

        ivReadStyleBreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReadStyle(ivReadStyleBreen);
                if(readStyleListener!=null)
                    readStyleListener.onChange(v,ReadStyle.breen);
            }
        });

        ivReadStyleBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReadStyle(ivReadStyleBlue);
                if(readStyleListener!=null)
                    readStyleListener.onChange(v,ReadStyle.blueDeep);
            }
        });

        ivReadStyleNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView nextImage=readstyleList.get((readstyleList.indexOf(lastReadStyle)+1)%ReadStyle.values().length);
                setReadStyle(nextImage);
                if(readStyleListener!=null)
                    readStyleListener.onChange(v,ReadStyle.get((readstyleList.indexOf(lastReadStyle))%ReadStyle.values().length));
            }
        });

        tvTextFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, FontsActivity.class);
                context.startActivityForResult(intent,1);
            }
        });


        //简繁切换
        tvTradAndSimp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=false;
                if(tvTradAndSimp.getText().equals("简")){
                    tvTradAndSimp.setText("繁");
                    flag=true;
                }
                else{
                    flag=false;
                    tvTradAndSimp.setText("简");
                }

                if(tradSimpListener!=null)
                    tradSimpListener.changeLanguage(view,flag);
            }
        });


        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        return dialog;

    }

    private static void setReadStyle(ImageView currentReadStyle){
        if(lastReadStyle!=null){
            lastReadStyle.setSelected(false);
        }

        lastReadStyle=currentReadStyle;
        lastReadStyle.setSelected(true);

    }


    //章节变化回调
    public interface ChapterChangeListener{

        void onClick(View view,int chapter);

    }


    //缓存进度回调
    public interface DownloadProgressListener{
        void onClick(View view,int progress);
    }

    //日间 夜间模式回调
    public interface DayAndNightListener{
        void onClick(View view);
    }

    //字体大小回调
    public interface TextSizeListener{
        void onClick(View view,int textSize);
    }

    //亮度变化回调
    public interface BrightnessListener{
        void onChange(View view,int brightness,boolean flag);
    }

    //阅读模式回调
    public interface ReadStyleListener{

        void onChange(View view, ReadStyle readStyle);
    }

    //简繁切换回调
    public interface TradSimpListener{


        void changeLanguage(View view,boolean isTrad);
    }


    //自动滚屏速度
    public interface ScrollSpeedListener{


        void changeSpeed(View view,int progress,boolean isAutoScroll);
    }
}
