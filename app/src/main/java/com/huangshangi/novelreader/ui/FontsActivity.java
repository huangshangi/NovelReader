package com.huangshangi.novelreader.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huangshangi.novelreader.Font;
import com.huangshangi.novelreader.R;

import java.util.ArrayList;
import java.util.List;

public class FontsActivity extends AppCompatActivity {

    LinearLayout llBack;
    TextView tvTitle;
    ListView listView;

    FontsAdapter fontsAdapter;

    List<Font>list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonts);
        initFonts();
        initViews();
        initDatas();
        initListeners();
    }


    private void initViews(){

        llBack=findViewById(R.id.ll_title_back);
        tvTitle=findViewById(R.id.tv_title_text);
        listView=findViewById(R.id.lv_fonts);
        fontsAdapter=new FontsAdapter(this,list);
    }

    private void initDatas(){
        listView.setAdapter(fontsAdapter);
    }


    private void initListeners(){
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initFonts(){
        list=new ArrayList<>();
        list.add(Font.默认字体);
        list.add(Font.方正楷体);
        list.add(Font.经典宋体);
        list.add(Font.方正行楷);
        list.add(Font.迷你隶书);
        list.add(Font.方正黄草);
        list.add(Font.书体安景臣钢笔行书);

    }



}
