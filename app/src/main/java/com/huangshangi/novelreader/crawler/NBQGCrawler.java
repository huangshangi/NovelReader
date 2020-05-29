package com.huangshangi.novelreader.crawler;

import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.bean.BookType;
import com.huangshangi.novelreader.bean.Chapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.List;


/*
    源:新笔趣阁
    源地址:http://www.xbiquge.la/


 */
public class NBQGCrawler implements BaseCrawler{

    final String BASEURL="http://www.xbiquge.la/";



    static NBQGCrawler instance;


    public  static NBQGCrawler getInstance() {

        if(instance==null)
            if(instance==null)
                instance=new NBQGCrawler();
        return instance;
    }

    @Override
    public List<BookType> getBookTypeList(String html) {
        List<BookType>list=new ArrayList<>();

        Document document=Jsoup.parse(html);

        Elements elements=document.select("div.nav ul li a");

        for(Element element:elements){
            BookType bookType=new BookType();
            String type=element.text();
            String url=element.attr("href");

            bookType.setTypeUrl(url);
            bookType.setTypeName(type);//好看的玄幻小说最近更新列表
            list.add(bookType);
        }
        return list;
    }
    @Override
    public List<Book> getBooksList(String html) {
        List<Book>list=new ArrayList<>();

        Document document=Jsoup.parse(html);

        Elements elements=document.select("div.l ul li ");

        for(Element element:elements){
            Book book=new Book();
            String bookName=element.selectFirst("span.s2 a").text();
            String bookUrl=element.selectFirst("span.s2 a").attr("href");
            String type=element.selectFirst(".s1").text();
            book.setBookName(bookName);
            book.setBookIntroUrl(bookUrl);
            book.setBookType(type);//好看的玄幻小说最近更新列表
            list.add(book);
        }
        return list;
    }
    @Override
    public Book getBookIntro(String html) {
        Book book=new Book();
        Document document= Jsoup.parse(html);
        Element imageUrl=document.selectFirst("#fmimg img");
        Element bookName=document.selectFirst("#maininfo #info h1");
        Element author=document.selectFirst("#maininfo #info p");
        Element updatelast=document.select("#maininfo #info p").get(2);
        Element lastestChapter=document.select("#maininfo #info p").get(3).selectFirst("a");

        Element intro=document.select("#intro p").get(1);
        Element bookType=document.select(".con_top a").get(2);

        book.setAuthorName(author.text());//作    者：耳根
        book.setBookImage(imageUrl.attr("src"));
        book.setBookIntro(intro.text());
        book.setBookName(bookName.text());
        book.setBookType(bookType.text());//修真小说
        book.setUpdateDate(updatelast.text());//最后更新：2020-05-21 17:50:00
        book.setLastestChapter(lastestChapter.text());//第946章 移花接木！
        return book;
    }

    @Override
    public List<Chapter> getChapters(String bookId,String html) {
        List<Chapter>list=new ArrayList<>();
        Document document=Jsoup.parse(html);

        Elements elements=document.select("#list dl dd a");

        for( int i=0;i<elements.size();i++){
            Chapter chapter=new Chapter();
            Element element=elements.get(i);
            String url=BASEURL+element.attr("href");///10/10489/4534454.html
            String title=element.text();//写在连载前 第一章 我要减肥！
            chapter.setChapterId(i+1);
            chapter.setChapterName(title);
            chapter.setChapterUrl(url);
            list.add(chapter);
        }
        return list;
    }

    @Override
    public  String getContent(String html) {

        return Jsoup.parse(html).select("div#content").text();
    }
}
