package com.huangshangi.novelreader.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.callback.ResultCallback;
import com.huangshangi.novelreader.crawler.BQG5200Crawler;
import com.huangshangi.novelreader.util.StringUtil;
import com.huangshangi.novelreader.webapi.CommonApi;

import java.util.List;


public class SerachResultAdapter extends BaseAdapter {

    List<Book>list;

    Context context;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    initView((ViewHolder) msg.obj,msg.arg1);
                    break;
            }
        }
    };

    public SerachResultAdapter(Context context,List<Book>list) {
        super();
        this.context=context;
        this.list=list;
    }

    public void setList(List<Book>list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Book getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("测试","0");
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.activity_search_result_item,null);
            viewHolder.ivBookImage=convertView.findViewById(R.id.iv_book_img);
            viewHolder.tvBookName=convertView.findViewById(R.id.tv_book_name);
            viewHolder.tvBookAuthor=convertView.findViewById(R.id.tv_book_author);
            viewHolder.tvBookType=convertView.findViewById(R.id.tv_book_type);
            viewHolder.tvBookIntro=convertView.findViewById(R.id.tv_book_desc);
            convertView.setTag(viewHolder);
        }else
            viewHolder=(ViewHolder)convertView.getTag();

        //初始化内容
        initView(viewHolder,position);

        return convertView;
    }

    private void initView(final ViewHolder viewHolder, final int position){
            Book book=list.get(position);

            if(viewHolder==null)return;
            if(StringUtil.isEmpty(book.getBookImage())){

                CommonApi.HttpGet(book.getBookIntroUrl(),null,new ResultCallback(){

                    @Override
                    public void onFinish(Object object, int code) {

                        Book book0= BQG5200Crawler.getInstance().getBookIntro((String)object);
                        list.set(position,book0);
                        Message message=new Message();
                        message.what=1;
                        message.obj=viewHolder;
                        message.arg1=position;
                        handler.sendMessage(message);

                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
            }else{
                viewHolder.tvBookType.setText(book.getBookType());
                viewHolder.tvBookAuthor.setText(book.getAuthorName());
                viewHolder.tvBookIntro.setText(book.getBookIntro());
                viewHolder.tvBookName.setText(book.getBookName());
                Glide.with(context).load(book.getBookImage()).into(viewHolder.ivBookImage);
            }
    }



    class ViewHolder{


        ImageView ivBookImage;
        TextView tvBookName;
        TextView tvBookAuthor;
        TextView tvBookType;
        TextView tvBookIntro;
    }
}
