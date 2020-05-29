package com.huangshangi.novelreader.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huangshangi.novelreader.Font;
import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.ReadingSetting;
import com.huangshangi.novelreader.TypeFace;
import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.bean.Chapter;
import com.huangshangi.novelreader.callback.ResultCallback;
import com.huangshangi.novelreader.crawler.BQG5200Crawler;
import com.huangshangi.novelreader.dao.dbservice.ChapterDBService;
import com.huangshangi.novelreader.util.StringUtil;
import com.huangshangi.novelreader.webapi.CommonApi;

import java.util.ArrayList;
import java.util.List;


public class ReadContentAdapter extends RecyclerView.Adapter {

    Context context;

    List<Chapter>list;

    Book book;

    Typeface typeface;

    ReadingSetting setting;

    ChapterDBService chapterDBService;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    public ReadContentAdapter(Context context,List<Chapter>list,Book book){
        this.context=context;
        this.list=list==null?new ArrayList<Chapter>():list;
        this.book=book;
        setting=ReadingSetting.getInstance();
        chapterDBService=new ChapterDBService();
    }

    public void setList(List<Chapter> list) {
        this.list=list==null?new ArrayList<Chapter>():list;
        notifyDataSetChanged();
    }

    //加载某一章的内容
    private void getChapterContent(final Chapter chapter,final ViewHolder viewHolder){

        if(viewHolder!=null)
            viewHolder.tv_tips.setVisibility(View.GONE);



        //为判断是否为空  若为空 需重新获取
        if(StringUtil.isEmpty(chapter.getChapterContent())){

            CommonApi.getChapterContent(chapter.getChapterUrl(), new ResultCallback() {
                @Override
                public void onFinish(Object object, int code) {

                    final String content=BQG5200Crawler.getInstance().getContent((String)object);

                    chapter.setChapterContent(content);

                    chapterDBService.updateCacheChapter(chapter);
                    if(viewHolder!=null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.tv_title.setText(chapter.getChapterName());
                                viewHolder.tv_content.setText(StringUtil.transformTradition(content,setting.isTradition()));
                                viewHolder.tv_tips.setVisibility(View.GONE);
                            }
                        });

                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();

                }
            });
        }

        else if(viewHolder!=null){
            viewHolder.tv_title.setText(chapter.getChapterName());
            viewHolder.tv_content.setText(StringUtil.transformTradition(chapter.getChapterContent(),setting.isTradition()));
        }



    }

    //预加载上一章的内容
    private void getLastChapterContent(int position){
        if(position-1>=0)
            getChapterContent(list.get(position-1),null);
    }

    //预加载下一章的内容
    private void getNextChapterContent(int position){
        if(position+1<list.size())
            getChapterContent(list.get(position+1),null);
    }

    //因字体 而重绘
    public void notifyRepaint(){
        if(setting.getTypeFace()== Font.默认字体)
            typeface=null;
        else
            typeface=Typeface.createFromAsset(context.getAssets(),setting.getTypeFace().path);

        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_reading_item,null);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder=(ViewHolder)holder;
        Chapter chapter=list.get(position);


        viewHolder.tv_title.setTypeface(typeface);
        viewHolder.tv_content.setTypeface(typeface);
        viewHolder.tv_tips.setTypeface(typeface);

        if(setting.isDayMode()){
            viewHolder.tv_title.setTextColor(context.getResources().getColor(setting.getTextColor()));
            viewHolder.tv_content.setTextColor(context.getResources().getColor(setting.getTextColor()));
            viewHolder.tv_tips.setTextColor(context.getResources().getColor(setting.getTextColor()));

        }else{

            viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.sys_night_word));
            viewHolder.tv_content.setTextColor(context.getResources().getColor(R.color.sys_night_word));
            viewHolder.tv_tips.setTextColor(context.getResources().getColor(R.color.sys_night_word));
        }

        viewHolder.tv_tips.setTextSize(setting.getTextSize());
        viewHolder.tv_content.setTextSize(setting.getTextSize());
        viewHolder.tv_title.setTextSize(setting.getTextSize()+2);

        //待绑定
        getChapterContent(chapter,viewHolder);

        getLastChapterContent(position);

        getNextChapterContent(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title;

        TextView tv_content;

        TextView tv_tips;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title=itemView.findViewById(R.id.tv_title);
            tv_content=itemView.findViewById(R.id.tv_content);
            tv_tips=itemView.findViewById(R.id.tv_loading_error_tips);
        }
    }
}
