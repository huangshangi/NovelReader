package com.huangshangi.novelreader.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.callback.ResultCallback;
import com.huangshangi.novelreader.crawler.BQG5200Crawler;
import com.huangshangi.novelreader.crawler.NBQGCrawler;
import com.huangshangi.novelreader.util.StringUtil;
import com.huangshangi.novelreader.webapi.CommonApi;

import java.util.ArrayList;
import java.util.List;


public class BookStoreContentAdapter extends RecyclerView.Adapter {

    List<Book>list;

    Context context;

    BookClickListener bookClickListener;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    int positon=(int)msg.arg1;
                    ViewHolder viewHolder=(ViewHolder)msg.obj;
                    initView(list.get(positon),viewHolder);
                    break;
            }
        }
    };

    public BookStoreContentAdapter(Context context,List<Book>list,BookClickListener bookClickListener) {
        super();
        this.context=context;
        this.list=list==null?new ArrayList<Book>():list;
        this.bookClickListener=bookClickListener;
    }

    public void setList(List<Book> list) {
        this.list=list==null?new ArrayList<Book>():list;
    }

    private void initView(Book book,ViewHolder viewHolder){
        if(viewHolder==null)
            return;
        viewHolder.tvBookIntro.setText(book.getBookIntro());
        viewHolder.tvBookAuthor.setText(book.getAuthorName());
        viewHolder.tvBookName.setText(book.getBookName());
        Glide.with(context).load(book.getBookImage()).into(viewHolder.ivBookImage);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.bookstore_content_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder=(ViewHolder)holder;
        Book book=list.get(position);

        if(StringUtil.isEmpty(book.getBookImage())){
            CommonApi.getChapterContent(book.getBookIntroUrl(), new ResultCallback() {
                @Override
                public void onFinish(Object object, int code) {
                    String html=(String)object;
                    Book obj= BQG5200Crawler.getInstance().getBookIntro(html);
                    list.set(position,obj);
                    handler.sendMessage(handler.obtainMessage(1,position,0,viewHolder));
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }else{
            initView(book,viewHolder);
        }

        if(bookClickListener!=null)
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookClickListener.onClick(v,position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvBookName;
        TextView tvBookIntro;
        TextView tvBookAuthor;
        ImageView ivBookImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookAuthor=itemView.findViewById(R.id.tv_book_author);
            tvBookIntro=itemView.findViewById(R.id.tv_book_desc);
            tvBookName=itemView.findViewById(R.id.tv_book_name);
            ivBookImage=itemView.findViewById(R.id.iv_book_img);
        }
    }

    interface BookClickListener{

        void onClick(View view,int position);
    }
}
