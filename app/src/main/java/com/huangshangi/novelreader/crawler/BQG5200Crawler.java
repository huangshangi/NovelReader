package com.huangshangi.novelreader.crawler;

import android.widget.Toast;

import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.bean.BookType;
import com.huangshangi.novelreader.bean.Chapter;
import com.huangshangi.novelreader.util.StringUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.List;

/*
    源：笔趣阁
    源地址:http://www.ibqg5200.com/
 */
public class BQG5200Crawler implements BaseCrawler {

    final String BASEURL="http://www.ibqg5200.com";

    static BQG5200Crawler instance;

    public  static BQG5200Crawler getInstance() {

        if(instance==null)
            if(instance==null)
                instance=new BQG5200Crawler();
        return instance;
    }

    @Override
    public List<Book> getBooksList(String html) {
        List<Book>list=new ArrayList<>();

        Document document=Jsoup.parse(html);

        Element elementType=document.selectFirst(".list_center span.update_icon");
        Elements elements=document.select("div.update_list ul li");

        for(Element element:elements){
            Book book=new Book();
            book.setBookIntroUrl(element.selectFirst("span a").attr("href"));
            book.setBookName(element.selectFirst("span a").text());
            book.setBookType(elementType.text());//武侠修真·小说列表
            list.add(book);
        }

        return list;
    }

    @Override
    public List<BookType> getBookTypeList(String html) {
        List<BookType>list=new ArrayList<>();

        Document document=Jsoup.parse(html);

        Elements elements=document.select(".nav ul li a");

        for(Element element:elements){
            BookType bookType=new BookType();
            bookType.setTypeUrl(element.attr("href").toString());

            bookType.setTypeName(element.text());//武侠修真·小说列表

            list.add(bookType);
        }

        return list;
    }

    @Override
    public Book getBookIntro(String html) {

        Book book=new Book();
        Document document= Jsoup.parse(html);

        Element elementImageUrl=document.selectFirst("#bookimg img");
        Element elementBookName=document.selectFirst(".booktitle h1");
        Element elementType=document.selectFirst("#count span");
        Element elementAuthor=document.selectFirst(".booktitle #author");
        Element elementUpdateDate=document.selectFirst(".new .new_t");
        Element elementLastest=document.selectFirst(".new .new_l a");
        Element elementIntro=document.selectFirst("#bookintro p");
        Element elementBookUrl=document.selectFirst("#bookinfo .motion a");
        if(elementImageUrl!=null)
            book.setBookImage(elementImageUrl.attr("src"));
        book.setBookName(elementBookName.text());//玄幻魔法
        book.setAuthorName(elementAuthor.text());//作者：净无痕
        book.setBookType(elementType.text());
        book.setUpdateDate(elementUpdateDate.text());//最后更新：2020-05-21
        book.setLastestChapter(elementLastest.text());//第1851章 兵临
        book.setBookIntro(elementIntro.text());
        book.setBookChapterUrl(elementBookUrl.attr("href"));
        book.setBookId(StringUtil.getHash(book.getBookName()+book.getAuthorName()));
        return book;
    }

    @Override
    public List<Chapter> getChapters(String bookId,String html) {

        List<Chapter>list=new ArrayList<>();
        Document document=Jsoup.parse(html);


        Elements elements=document.select("#readerlist ul li");

        for(int i=0;i<elements.size();i++){
            Element element=elements.get(i);
            Chapter chapter=new Chapter();

            chapter.setChapterId(i+1);
            chapter.setChapterName(element.select("a").text());
            chapter.setChapterUrl(BASEURL+element.select("a").attr("href"));
            chapter.setBookId(bookId);
            chapter.setId(bookId+chapter.getChapterId());
            list.add(chapter);
        }

        return list;
    }

    @Override
    public String getContent(String html) {
        return Jsoup.parse(html).select("#content").text();
    }

    public List<Book>getBookListByKey(String html){

        List<Book>list=new ArrayList<>();
        Document document=Jsoup.parse(html);

        Elements elements=document.select("tbody tr");
        for(int i=1;i<elements.size();i++){
            Element element=elements.get(i);
            Book book=new Book();
            String bookName=element.selectFirst(".odd a").text();
            String bookUrl=element.selectFirst(".odd a").attr("href");
            String updateChapter=element.select("td.even a").get(0).text();
            String authorName=element.select("td:nth-child(3)").get(0).text();
            String updateTime=element.select("td:nth-child(3)").get(0).text();

            book.setBookName(bookName);
            book.setUpdateDate(updateTime);
            book.setBookIntroUrl(bookUrl);
            book.setLastestChapter(updateChapter);
            book.setAuthorName(authorName);
            book.setBookId(StringUtil.getHash(book.getBookName()+book.getAuthorName()));
            list.add(book);
        }
        return list;
    }
}
