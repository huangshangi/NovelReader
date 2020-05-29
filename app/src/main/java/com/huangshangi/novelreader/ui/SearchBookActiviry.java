package com.huangshangi.novelreader.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.bean.SearchHistory;
import com.huangshangi.novelreader.callback.ResultCallback;
import com.huangshangi.novelreader.crawler.BQG5200Crawler;
import com.huangshangi.novelreader.dao.dbservice.SearchHistoryDBService;
import com.huangshangi.novelreader.util.SysUtil;
import com.huangshangi.novelreader.webapi.CommonApi;

import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class SearchBookActiviry extends AppCompatActivity implements TagGroup.OnTagClickListener {

    LinearLayout llTitleBack;//返回按钮
    TextView tvTitleText;//标题内容
    EditText etSearchText;//编辑框内容
    TextView tvSearchButton;//搜索按钮
    TextView tvTitleName;//标题名称
    TagGroup tgSuggestContent;//推荐的内容
    LinearLayout llChangeSuggest;//换一批按钮
    LinearLayout llChangeView;//换一批界面
    ListView lvHistoryList;//历史记录
    LinearLayout llClearHistory;//清空历史记录
    LinearLayout llClearHistoryView;//清空历史记录view
    ListView lvSearcchContentList;//搜索的结果listView

    SearchHistoryDBService searchHistoryDBService;

    List<Book>findList;//搜索结果

    List<SearchHistory>historyList;

    HistorySerachAdapter historySerachAdapter;//历史记录适配器

    SerachResultAdapter serachResultAdapter;//搜索结果适配器

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what){
                case 1:
                    lvSearcchContentList.setVisibility(View.VISIBLE);
                    serachResultAdapter.setList(findList);

                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search_book_activiry);

        initViews();

        initDatas();

        initListeners();
    }



    private void initViews(){


        llTitleBack=findViewById(R.id.ll_title_back);
        tvTitleText=findViewById(R.id.tv_title_text);
        etSearchText=findViewById(R.id.et_search_key);
        tvSearchButton=findViewById(R.id.tv_search_conform);
        tgSuggestContent=findViewById(R.id.tg_suggest_book);
        llChangeSuggest=findViewById(R.id.ll_refresh_suggest_books);
        lvHistoryList=findViewById(R.id.lv_history_list);
        llClearHistory=findViewById(R.id.ll_clear_history);
        lvSearcchContentList=findViewById(R.id.lv_search_books_list);
        llChangeView=findViewById(R.id.ll_suggest_books_view);
        tvTitleName=findViewById(R.id.tv_title_text);
        llChangeView.setVisibility(View.VISIBLE);

        findList=new ArrayList<>();
        historyList=new ArrayList<>();
        llClearHistoryView=findViewById(R.id.ll_history_view);
        historySerachAdapter=new HistorySerachAdapter(this,historyList);
        serachResultAdapter=new SerachResultAdapter(this,findList);

        lvSearcchContentList.setAdapter(serachResultAdapter);

        lvHistoryList.setAdapter(historySerachAdapter);

        searchHistoryDBService=new SearchHistoryDBService();

        lvHistoryList.setVisibility(View.VISIBLE);
    }


    private void initDatas(){
        tgSuggestContent.setTags("绝望黎明","绝望教师","绝望明天","绝望后天","绝望今天","绝望晚上");

        historyList=searchHistoryDBService.getSearchHistorys();

        historySerachAdapter.setList(historyList);
        tvTitleName.setText("搜索");
    }


    private void initListeners(){

        //返回按钮
        llTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=etSearchText.getText().toString();
                SysUtil.hideKeyboard(SearchBookActiviry.this);
                //进行搜索操作
                findBooks(key);
            }
        });

        tgSuggestContent.setOnTagClickListener(this);


        //换一批推荐内容
        llChangeSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //换一批操作未实现

            }
        });

        lvHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                findBooks(historyList.get(position).getSearchContent());
            }
        });

        lvSearcchContentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进行bookInfo界面
                Intent intent=new Intent(SearchBookActiviry.this,BookInfoActivity.class);
                intent.putExtra("BOOK", findList.get(position));

                startActivity(intent);
            }
        });
    }


    //执行搜索操作
    private void findBooks(String key){

        etSearchText.setText(key);
        etSearchText.setSelection(key.length());

        //将搜索数据存入数据库
        //首先检查数据库中是否存在
        for(int i=0;i<historyList.size();i++){
            SearchHistory searchHistory= historyList.get(i);
            if(searchHistory.getSearchContent().equals(key)){
                searchHistory.setSearchTime(SysUtil.getCurrentTime());

                break;
            }
        }

        historyList.add(new SearchHistory(key,SysUtil.getCurrentTime()));

        historySerachAdapter.setList(historyList);

        llChangeView.setVisibility(View.GONE);
        llClearHistoryView.setVisibility(View.GONE);
        CommonApi.getBookListByKey("http://www.ibqg5200.com/modules/article/search.php",key, new ResultCallback() {
            @Override
            public void onFinish(Object object, int code) {
                String html=(String)object;

                findList=BQG5200Crawler.getInstance().getBookListByKey(html);

                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onTagClick(String tag) {

        findBooks(tag);

    }



    @Override
    protected void onDestroy() {


        //提交历史记录

        searchHistoryDBService.deleteSeachHistorys();
        searchHistoryDBService.addSearchHistorys(historyList);

        super.onDestroy();
    }
}
