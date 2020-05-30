package com.huangshangi.novelreader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.huangshangi.novelreader.Font;
import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.ReadingSetting;
import com.huangshangi.novelreader.TypeFace;

import java.util.List;

public class FontsAdapter extends BaseAdapter {

    List<Font>list;
    Activity context;
    ReadingSetting setting;

    public FontsAdapter(Activity context,List<Font>list){
        this.context=context;
        this.list=list;
        setting=ReadingSetting.getInstance();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Font getItem(int position) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.fonts_item_layout,null);
            viewHolder.tvEmaple=convertView.findViewById(R.id.tv_font_example);
            viewHolder.tvFontName=convertView.findViewById(R.id.tv_font_name);
            viewHolder.btnUseOrUseless=convertView.findViewById(R.id.btn_font_use);
            convertView.setTag(viewHolder);
        }else
            viewHolder=(ViewHolder)convertView.getTag();

        initView(getItem(position),viewHolder);

        return convertView;
    }

    private void initView(final Font font, ViewHolder viewHolder){

        Typeface typeface=null;
        if(font!=Font.默认字体)
            typeface=Typeface.createFromAsset(context.getAssets(), font.path);

        viewHolder.tvEmaple.setTypeface(typeface);
        viewHolder.tvFontName.setText(font.toString());

        if(setting.getTypeFace()==font){
            viewHolder.btnUseOrUseless.setBackgroundResource(R.drawable.font_using_btn_bg);
            viewHolder.btnUseOrUseless.setTextColor(context.getResources().getColor(R.color.sys_font_using_btn));
            viewHolder.btnUseOrUseless.setText("正在使用");
            viewHolder.btnUseOrUseless.setOnClickListener(null);
        }else{
            viewHolder.btnUseOrUseless.setBackgroundResource(R.drawable.font_use_btn_bg);
            viewHolder.btnUseOrUseless.setTextColor(context.getResources().getColor(R.color.sys_font_use_btn));
            viewHolder.btnUseOrUseless.setText("立即使用");
            viewHolder.btnUseOrUseless.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setting.setTypeFace(font);
                    Intent intent=new Intent();
                    intent.putExtra("FONT",font);
                    context.setResult(Activity.RESULT_OK,intent);
                    notifyDataSetChanged();
                }
            });
        }
    }

    class ViewHolder{

        TextView tvEmaple;

        TextView tvFontName;

        Button btnUseOrUseless;

        public ViewHolder(){

        }
    }
}
