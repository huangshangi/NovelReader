package com.huangshangi.novelreader.dao.dbservice;

import android.util.Log;

import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.dao.greendao.BookDao;
import com.huangshangi.novelreader.util.StringUtil;

import java.util.List;

public class BookDBService extends BaseDBService {


    @Override
    public void addEntity(Object object) {
        Book obj=(Book)object;
        obj.setBookId(StringUtil.getHash(obj.getAuthorName()+obj.getBookName()));

        super.addEntity(obj);

    }

    //获取书架中的书籍
    public List<Book>getShelfBooks(){

        List<Book>list=(List<Book>) getEntity(Book.class,"ORDER BY "+BookDao.Properties.AddShelfTime.columnName,null);

        return list;
    }

    //根据bookid查询book
    public Book getBookByBookId(String bookId){

        List<Book>list=(List<Book>)getEntity(Book.class,"WHERE "+ BookDao.Properties.BookId.columnName+"=?;",new String[]{bookId});

        if(list.size()==0)
            return null;
        else
            return list.get(0);

    }

}
