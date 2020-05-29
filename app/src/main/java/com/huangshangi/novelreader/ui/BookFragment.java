package com.huangshangi.novelreader.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.HandyGridViewAdapter;
import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.dao.dbservice.BookDBService;
import com.huangshangi.novelreader.util.DensityUtil;
import com.huxq17.handygridview.HandyGridView;
import com.huxq17.handygridview.listener.IDrawer;
import com.huxq17.handygridview.listener.OnItemCapturedListener;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment implements HandyGridViewAdapter.DeleteBookItemListener {

    private MainActivity mainActivity;

    private HandyGridView mGridView;
    private List<Book> list;

    private LinearLayout llTitle;
    private LinearLayout llTitleEdit;
    private LinearLayout llTitleBack;

    private TextView tvTitleName;

    BookDBService bookDBService;

    private HandyGridViewAdapter adapter;
    private ViewGroup outLayout;

    TextView tvFinish;


    public BookFragment(MainActivity mainActivity){
        this.mainActivity=mainActivity;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_book,container,false);


        initView(view);
        setMode(HandyGridView.MODE.LONG_PRESS);




        return view;
    }





    private void initView(View view) {


        bookDBService=new BookDBService();
        list=bookDBService.getShelfBooks();
        outLayout = view.findViewById(R.id.out_layout);

        mGridView = view.findViewById(R.id.grid_tips);

        llTitle=view.findViewById(R.id.ll_title);
        llTitleEdit=view.findViewById(R.id.ll_title_edit);
        tvTitleName=view.findViewById(R.id.tv_title_text);
        llTitleBack=view.findViewById(R.id.ll_title_back);
        tvFinish=view.findViewById(R.id.tv_edit_finish);

        llTitleBack.setVisibility(View.GONE);
        list=bookDBService.getShelfBooks();
        adapter = new HandyGridViewAdapter(mainActivity, list,this);
        mGridView.setAdapter(adapter);


        mGridView.setAutoOptimize(false);

        tvTitleName.setText("书架");

        mGridView.setScrollSpeed(750);

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMode(HandyGridView.MODE.LONG_PRESS);
                llTitleEdit.setVisibility(View.GONE);
                llTitle.setVisibility(View.VISIBLE);
            }
        });

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                setMode(HandyGridView.MODE.LONG_PRESS);
                Log.e("这是测试","测试内容");
                llTitleEdit.setVisibility(View.VISIBLE);
                if (!mGridView.isTouchMode()&&!mGridView.isNoneMode() && !adapter.isFixed(position)) {

                    setMode(HandyGridView.MODE.TOUCH);

                    return true;
                }
                return false;
            }
        });

        mGridView.setOnItemCapturedListener(new OnItemCapturedListener() {
            @Override
            public void onItemCaptured(View v, int position) {
                v.setScaleX(1.2f);
                v.setScaleY(1.2f);

            }

            @Override
            public void onItemReleased(View v, int position) {
                v.setScaleX(1f);
                v.setScaleY(1f);

                setMode(HandyGridView.MODE.TOUCH);

            }

        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //进入阅读界面
                Intent intent=new Intent(getActivity(),ReadActivity.class);
                intent.putExtra("BOOK",list.get(position));
                startActivity(intent);
            }
        });

    }




    private void setMode(HandyGridView.MODE mode) {

        mGridView.setMode(mode);

        adapter.setInEditMode(mode == HandyGridView.MODE.TOUCH);
    }


    @Override
    public void onClick(int positon) {
        Book book=list.get(positon);
        bookDBService.deleteEntity(book);
        list.remove(positon);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }
}
