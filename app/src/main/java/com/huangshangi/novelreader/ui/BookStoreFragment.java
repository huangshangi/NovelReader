package com.huangshangi.novelreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.bean.BookType;
import com.huangshangi.novelreader.callback.ResultCallback;
import com.huangshangi.novelreader.crawler.BQG5200Crawler;
import com.huangshangi.novelreader.crawler.NBQGCrawler;
import com.huangshangi.novelreader.webapi.CommonApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookStoreFragment extends Fragment implements BookTypeAdapter.TabClickListener, BookStoreContentAdapter.BookClickListener {

    RecyclerView rvTypeList;//左侧
    RecyclerView rvContentList;//右侧
    private ProgressBar pbLoading;//加载条

    BookTypeAdapter bookTypeAdapter;//类型适配器

    BookStoreContentAdapter bookStoreContentAdapter;//书适配器

    LinearLayoutManager llTypemanager;

    LinearLayoutManager llContentmanager;

    //标题栏
    LinearLayout llTitleBack;
    ImageView lvTitleFind;
    TextView tvTitleName;

    List<BookType>typeList;

    Map<String,List<Book>> hashmap;//用于缓存已经获取的信息

    int currentStyleTab;//用于记录当前被选择的tab

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    typeList=(List<BookType>)msg.obj;
                    initTypeList();
                    getContentDatas(currentStyleTab);
                    break;
                case 2:
                    initContentList();
                    pbLoading.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bookstore,container,false);

        initViews(view);

        getTypeDatas();

        initListeners();

        return view;
    }

    private void initViews(View view){

        rvTypeList=view.findViewById(R.id.rv_type_list);
        rvContentList=view.findViewById(R.id.rv_book_list);

        pbLoading=view.findViewById(R.id.pb_loading);

        hashmap=new HashMap<>();

        llTypemanager=new LinearLayoutManager(getActivity());
        llTypemanager.setOrientation(RecyclerView.VERTICAL);
        rvTypeList.setLayoutManager(llTypemanager);
        bookTypeAdapter=new BookTypeAdapter(getActivity(),null,this);
        rvTypeList.setAdapter(bookTypeAdapter);
        llTitleBack=view.findViewById(R.id.ll_title_back);
        lvTitleFind=view.findViewById(R.id.iv_title_search);
        tvTitleName=view.findViewById(R.id.tv_title_text);
        llContentmanager=new LinearLayoutManager(getActivity());
        llContentmanager.setOrientation(RecyclerView.VERTICAL);
        rvContentList.setLayoutManager(llContentmanager);

        bookStoreContentAdapter=new BookStoreContentAdapter(getActivity(),null,this);
        rvContentList.setAdapter(bookStoreContentAdapter);

        tvTitleName.setText("书库");
        llTitleBack.setVisibility(View.GONE);

    }

    private void initListeners(){
        lvTitleFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SearchBookActiviry.class);
                startActivity(intent);
            }
        });
    }


    private void getTypeDatas(){
        pbLoading.setVisibility(View.VISIBLE);
        CommonApi.getChapterContent("http://www.ibqg5200.com/", new ResultCallback() {
            @Override
            public void onFinish(Object object, int code) {
                String res=(String)object;
                List<BookType>list= BQG5200Crawler.getInstance().getBookTypeList(res);
                Message message=new Message();
                message.what=1;
                message.obj=list;
                handler.sendMessage(message);

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();

            }
        });
    }

    private void getContentDatas(int position){
        pbLoading.setVisibility(View.VISIBLE);
        if(typeList.size()==0)
            return;
        final BookType bookType=typeList.get(position);

        if(hashmap.containsKey(bookType.getTypeName()))
            handler.sendMessage(handler.obtainMessage(2));
        else
        CommonApi.getChapterContent(bookType.getTypeUrl(), new ResultCallback() {
            @Override
            public void onFinish(Object object, int code) {
                if(code==200){
                    String html=(String)object;
                    List<Book> bookList=BQG5200Crawler.getInstance().getBooksList(html);

                    hashmap.put(bookType.getTypeName(),bookList);

                    Message message=new Message();
                    message.what=2;
                    handler.sendMessage(message);

                }

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();

            }
        });
    }
    private void initTypeList(){

        bookTypeAdapter.setList(typeList);
        bookTypeAdapter.notifyDataSetChanged();
    }

    private void initContentList(){
        List<Book>bookList=hashmap.get(typeList.get(currentStyleTab).getTypeName());

        bookStoreContentAdapter.setList(bookList);
        bookStoreContentAdapter.notifyDataSetChanged();

    }


    //点击类型切换
    @Override
    public void onclick(View view, int position) {
        currentStyleTab=position;
        getContentDatas(position);
    }

    //点击书籍
    @Override
    public void onClick(View view, int position) {

        Book book=hashmap.get(typeList.get(currentStyleTab).getTypeName()).get(position);
        Log.e("测试自",book.getBookId()+" ");

        Intent intent=new Intent(getActivity(),BookInfoActivity.class);
        intent.putExtra("BOOK",book);
        startActivity(intent);

    }
}
