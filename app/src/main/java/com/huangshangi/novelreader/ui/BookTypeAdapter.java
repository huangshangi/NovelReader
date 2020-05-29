package com.huangshangi.novelreader.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.bean.BookType;

import java.util.ArrayList;
import java.util.List;

public class BookTypeAdapter extends RecyclerView.Adapter {

    List<BookType>list;

    Context context;

    TabClickListener tabClickListener;

    int currentSele;//当前被选择的标签页

    public BookTypeAdapter(Context context, List<BookType>list,TabClickListener tabClickListener) {
        super();
        this.context=context;
        this.list=list==null?new ArrayList<BookType>():list;
        this.tabClickListener=tabClickListener;

    }

    public void setList(List<BookType> list) {
        this.list=list==null?new ArrayList<BookType>():list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.bookstore_type_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder=(ViewHolder)holder;
        viewHolder.tvTypeName.setText(list.get(position).getTypeName());

        if(position==currentSele)
            viewHolder.itemView.setBackgroundResource(R.color.white);
        else
            viewHolder.itemView.setBackgroundResource(R.color.sys_book_type_bg);

        if(tabClickListener!=null)
            viewHolder.tvTypeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tabClickListener.onclick(v,position);
                    currentSele=position;
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTypeName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTypeName=itemView.findViewById(R.id.tv_type_name);
        }
    }

    interface TabClickListener{
        void onclick(View view,int position);
    }
}
