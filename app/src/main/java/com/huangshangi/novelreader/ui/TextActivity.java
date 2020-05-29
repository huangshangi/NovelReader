package com.huangshangi.novelreader.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.bean.BookType;
import com.huangshangi.novelreader.callback.ResultCallback;
import com.huangshangi.novelreader.crawler.NBQGCrawler;
import com.huangshangi.novelreader.webapi.CommonApi;

import java.util.ArrayList;
import java.util.List;

public class TextActivity extends AppCompatActivity {

    TextView textView;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    textView.setText((String)msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        textView=findViewById(R.id.textView);



        List<BookType> list=new ArrayList<>();

        CommonApi.getChapterContent("http://www.xbiquge.la/10/10489/9687224.html",new ResultCallback(){

            @Override
            public void onFinish(Object object, int code) {
                String content=(String)object;
                Message message=new Message();

                message.what=1;
                handler.sendMessage(message);
                Log.e("dddddddddddddddd","zhiing");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Log.e("dddddddddddddddd","111111111111111111111");
            }
        });
    }
}
