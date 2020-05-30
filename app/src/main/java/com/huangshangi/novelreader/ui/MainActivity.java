package com.huangshangi.novelreader.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.huangshangi.novelreader.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    BookFragment bookFragment;
    BookStoreFragment bookStoreFragment;
    DiscoverFragment discoverFragment;
    MineFragment mineFragment;


    Button bookButton;
    Button bookStoreButton;
    Button discoverButton;
    Button mineButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        initviews();
        setFragment(0);
    }


    //初始化控件
    public void initviews(){
        bookButton=(Button)findViewById(R.id.book_button);
        bookStoreButton=(Button)findViewById(R.id.bookstore_button);
        discoverButton=(Button)findViewById(R.id.discover_button);
        mineButton=(Button)findViewById(R.id.mine_button);

        bookButton.setOnClickListener(this);
        bookStoreButton.setOnClickListener(this);
        discoverButton.setOnClickListener(this);
        mineButton.setOnClickListener(this);

    }

    //显示第index个fragment
    public void setFragment(int index){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        hideFragments(fragmentTransaction);
        switch (index){
            case 0:
                if(bookFragment==null){
                    bookFragment=new BookFragment(this);
                    fragmentTransaction.add(R.id.container,bookFragment,"bookFragment");

                }else
                    fragmentTransaction.show(bookFragment);
                break;
            case 1:
                if(bookStoreFragment==null){
                    bookStoreFragment=new BookStoreFragment();
                    fragmentTransaction.add(R.id.container,bookStoreFragment,"bookstoreFragment");

                }else
                    fragmentTransaction.show(bookStoreFragment);
                break;
            case 2:
                if(discoverFragment==null){
                    discoverFragment=new DiscoverFragment();
                    fragmentTransaction.add(R.id.container,discoverFragment,"discoverFragment");

                }else
                    fragmentTransaction.show(discoverFragment);
                break;
            case 3:
                if(mineFragment==null){
                    mineFragment=new MineFragment();
                    fragmentTransaction.add(R.id.container,mineFragment,"mineFragment");

                }else
                    fragmentTransaction.show(mineFragment);
                break;
        }

        fragmentTransaction.commit();
    }

    //hide all fragmentss
    public void hideFragments(FragmentTransaction fragmentTransaction){
        if(bookFragment!=null)
            fragmentTransaction.hide(bookFragment);

        if(bookStoreFragment!=null)
            fragmentTransaction.hide(bookStoreFragment);

        if(discoverFragment!=null)
            fragmentTransaction.hide(discoverFragment);

        if(mineFragment!=null)
            fragmentTransaction.hide(mineFragment);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.book_button:setFragment(0);break;
            case R.id.bookstore_button:setFragment(1);break;
            case R.id.discover_button:setFragment(2);break;
            case R.id.mine_button:setFragment(3);break;
        }
    }
}
