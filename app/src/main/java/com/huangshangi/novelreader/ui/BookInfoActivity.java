package com.huangshangi.novelreader.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huangshangi.novelreader.R;
import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.dao.GreenDaoManager;
import com.huangshangi.novelreader.dao.dbservice.BookDBService;
import com.huangshangi.novelreader.dao.greendao.DaoSession;
import com.huangshangi.novelreader.util.StringUtil;

public class BookInfoActivity extends AppCompatActivity {

    LinearLayout llBack;//返回按钮
    TextView tvTitle;//文章标题
    ImageView ivBookImage;//文章图片url
    TextView tvBookName;//书名
    TextView tvBookAuthor;//作者
    TextView tvBookType;//类型
    TextView tvBookIntro;//简介
    Button btnShelf;//加入书架
    Button btnReading;//阅读按钮

    boolean isOnBookShelf;//是否已加入书架

    Book book;//上一个活动保存的数据

    Book temp;

    BookDBService bookDBService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

        setContentView(R.layout.activity_book_info);

        book=(Book) getIntent().getSerializableExtra("BOOK");


        initViews();
        initDatas();
        initListeners();
    }


    private void initViews(){
        llBack=findViewById(R.id.ll_title_back);
        tvTitle=findViewById(R.id.tv_title_text);
        ivBookImage=findViewById(R.id.iv_book_img);
        tvBookName=findViewById(R.id.tv_book_name);
        tvBookAuthor=findViewById(R.id.tv_book_author);
        tvBookType=findViewById(R.id.tv_book_type);
        tvBookIntro=findViewById(R.id.tv_book_desc);
        btnShelf=findViewById(R.id.btn_add_bookcase);
        btnReading=findViewById(R.id.btn_read_book);
        bookDBService=new BookDBService();

        temp=bookDBService.getBookByBookId(book.getBookId());

        if(temp!=null){
            book=temp;
            isOnBookShelf=true;
        }else
            isOnBookShelf=false;

    }

    private void initDatas(){


        //图片功能未实现
        tvTitle.setText(book.getBookName());
        tvBookName.setText(book.getBookName());
        tvBookAuthor.setText(book.getAuthorName());
        tvBookType.setText(book.getBookType());
        tvBookIntro.setText(book.getBookIntro());

        Glide.with(this).load(book.getBookImage()).into(ivBookImage);




        //追书按钮 不追按钮
        if(!isOnBookShelf)
            btnShelf.setText("加入书架");
        else
            btnShelf.setText("不追了");
    }

    private void initListeners(){

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加入书架功能待实现
                if(isOnBookShelf){
                    //从书架中移除
                    bookDBService.deleteEntity(book);
                    Toast.makeText(BookInfoActivity.this,"成功移除书籍",Toast.LENGTH_SHORT).show();
                    isOnBookShelf=false;
                    btnShelf.setText("加入书架");
                }else{
                    bookDBService.addEntity(book);
                    Toast.makeText(BookInfoActivity.this,"成功加入书架",Toast.LENGTH_SHORT).show();
                    isOnBookShelf=true;
                    btnShelf.setText("不追了");
                }



            }
        });

        btnReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookInfoActivity.this,ReadActivity.class);
                intent.putExtra("BOOK",book);
                startActivity(intent);
            }
        });
    }

}
