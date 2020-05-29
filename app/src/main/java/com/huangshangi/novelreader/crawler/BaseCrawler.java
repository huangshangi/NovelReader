package com.huangshangi.novelreader.crawler;


import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.bean.BookType;
import com.huangshangi.novelreader.bean.Chapter;

import java.util.List;

//
public interface BaseCrawler {



    public List<BookType> getBookTypeList(String html);

    public List<Book> getBooksList(String html);

    public Book getBookIntro(String html);

    public List<Chapter>getChapters(String bookId,String html);

    public String getContent(String html);

}
