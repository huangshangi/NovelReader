package com.huangshangi.novelreader.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.ReadingSetting;
import com.huangshangi.novelreader.bean.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChapterTitleChapter extends BaseAdapter {

    Context context;

    List<Chapter>list;

    ReadingSetting setting;

    public ChapterTitleChapter(Context context, List<Chapter> list){
        this.context=context;
        this.list=list==null?new ArrayList<Chapter>():list;
        setting=ReadingSetting.getInstance();
    }

    public void setList(List<Chapter> list) {
        this.list=list==null?new ArrayList<Chapter>():list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.activity_reading_listview_item,null);
            viewHolder=new ViewHolder();
            viewHolder.title=convertView.findViewById(R.id.tv_chapter_title);



            convertView.setTag(viewHolder);
        }else
            viewHolder=(ViewHolder) convertView.getTag();

        viewHolder.title.setText(list.get(position).getChapterName());//设置标题
        if(setting.isDayMode())
            viewHolder.title.setTextColor(context.getResources().getColor(setting.getTextColor()));
        else
            viewHolder.title.setTextColor(context.getResources().getColor(R.color.sys_night_word));
        return convertView;
    }


    class  ViewHolder{

        TextView title;


    }
}
