package com.huangshangi.novelreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huangshangi.novelreader.bean.Book;
import com.huxq17.handygridview.scrollrunner.OnItemMovedListener;

import java.util.ArrayList;
import java.util.List;

public class HandyGridViewAdapter extends BaseAdapter implements OnItemMovedListener {

    private Context context;
    private List<Book> list;
    DeleteBookItemListener deleteBookItemListener;

    public HandyGridViewAdapter(Context context, List<Book> list,DeleteBookItemListener deleteBookItemListener) {
        this.context = context;
        this.list=list==null?new ArrayList<Book>():list;
        this.deleteBookItemListener=deleteBookItemListener;
    }

    private GridView mGridView;
    private boolean inEditMode = false;

    public void setList(List<Book> list) {
        this.list=list==null?new ArrayList<Book>():list;
        notifyDataSetChanged();
    }

    public void setInEditMode(boolean inEditMode) {
        this.inEditMode = inEditMode;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_book_item, null);
            viewHolder.ivBookImg =  convertView.findViewById(R.id.iv_book_img);
            viewHolder.tvBookName = convertView.findViewById(R.id.tv_book_name);
            viewHolder.tvNoReadNum =  convertView.findViewById(R.id.tv_no_read_num);
            viewHolder.ivDelete = convertView.findViewById(R.id.iv_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initView(position, viewHolder);
        return convertView;
    }

    private void initView(final int position, final ViewHolder viewHolder) {
        final Book book = getItem(position);



        Glide.with(context).load(book.getBookImage()).into(viewHolder.ivBookImg);
        viewHolder.tvBookName.setText(book.getBookName());
        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(deleteBookItemListener!=null)
                    deleteBookItemListener.onClick(position);
                Toast.makeText(context,"您已经删除了",Toast.LENGTH_SHORT).show();


            }
        });


        if (inEditMode) {
            viewHolder.tvNoReadNum.setVisibility(View.GONE);
            viewHolder.ivDelete.setVisibility(View.VISIBLE);
            //viewHolder.ivBookImg.setOnClickListener(null);
        } else {
            viewHolder.ivDelete.setVisibility(View.GONE);
            if (book.getLeftToRead()!= 0) {
                viewHolder.tvNoReadNum.setVisibility(View.VISIBLE);
                if (book.getLeftToRead() > 99) {
                    viewHolder.tvNoReadNum.setText("...");
                } else {
                    viewHolder.tvNoReadNum.setText(String.valueOf(book.getLeftToRead()));
                }
            } else {
                viewHolder.tvNoReadNum.setVisibility(View.GONE);
            }



        }

    }



    @Override
    public void onItemMoved(int from, int to) {
        Book s = list.remove(from);
        list.add(to, s);
    }

    @Override
    public boolean isFixed(int position) {
        //When postion==0,the item can not be dragged.

        return false;
    }



    class ViewHolder {
        ImageView ivBookImg;
        TextView tvBookName;
        TextView tvNoReadNum;
        ImageView ivDelete;
    }

    public interface DeleteBookItemListener{


        void onClick(int positon);
    }

}
