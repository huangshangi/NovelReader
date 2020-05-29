package com.huangshangi.novelreader.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.bean.SearchHistory;

import java.util.List;

public class HistorySerachAdapter extends BaseAdapter {

    List<SearchHistory>list;

    Context context;

    public HistorySerachAdapter(Context context,List<SearchHistory>list) {
        super();
        this.list=list;
        this.context=context;
    }

    public void setList(List<SearchHistory>list){
        this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SearchHistory getItem(int position) {
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
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.activity_search_history_item,null);
            viewHolder.tvHistoryContent=convertView.findViewById(R.id.tv_history_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }

        //设置历史记录
        viewHolder.tvHistoryContent.setText(getItem(position).getSearchContent());

        return convertView;
    }

    class ViewHolder{


        TextView tvHistoryContent;
    }
}
