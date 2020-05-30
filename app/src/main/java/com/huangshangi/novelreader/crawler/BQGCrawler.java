package com.huangshangi.novelreader.crawler;

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
    源地址：http://www.biquge.tv/

 */
public class BQGCrawler implements BaseCrawler {

    final String BASEURL="http://www.biquge.tv";

    static BQGCrawler instance;

    public static BQGCrawler getInstance() {

        if(instance==null)
            if(instance==null)
                instance=new BQGCrawler();
        return instance;
    }

    @Override
    public List<BookType> getBookTypeList(String html) {
        List<BookType>list=new ArrayList<>();
        Document document= Jsoup.parse(html);

        Elements elements=document.select("div.nav ul li a");

        for(Element element:elements){
            BookType bookType=new BookType();
            bookType.setTypeName(element.text());
            bookType.setTypeUrl(element.attr("href"));


            list.add(bookType);
        }

        return list;
    }

    @Override
    public List<Book> getBooksList(String html) {
        List<Book>list=new ArrayList<>();
        Document document= Jsoup.parse(html);

        Elements elements=document.select("#newscontent .l ul li");

        for(Element element:elements){
            Book book=new Book();
            book.setBookType(element.selectFirst("span").text());
            book.setBookIntroUrl(element.select("span a").attr("href"));


            list.add(book);
        }

        return list;
    }

    @Override
    public Book getBookIntro(String html) {
        Book book=new Book();
        Document document=Jsoup.parse(html);

        Element elementBookName=document.selectFirst("#info h1");
        Element elementAuthor=document.selectFirst("#info p");
        Element elementUpdateDate=document.select("#info p").get(2);
        Element elementLastest=document.select("#info p").get(3);
        Element elementImageUrl=document.selectFirst("#fmimg img");
        Element elementIntro=document.selectFirst("#intro p");

        book.setBookName(elementBookName.text());
        book.setAuthorName(elementAuthor.text());//作    者：烧卖骑士
        book.setUpdateDate(elementUpdateDate.text());//最后更新：2020-05-21 22:04:29
        book.setLastestChapter(elementLastest.text());//第九十五章 云动砂隐村
        book.setBookImage(BASEURL+elementImageUrl.attr("src"));
        book.setBookIntro(elementIntro.text());
        book.setBookId(StringUtil.getHash(book.getBookName()+book.getAuthorName()));
        return book;
    }

    @Override
    public List<Chapter> getChapters(String bookId,String html) {

        List<Chapter>list=new ArrayList<>();
        Document document=Jsoup.parse(html);

        Elements elements=document.select("#list dl dt:nth-child(2) +dd a");

        for(int i=0;i<elements.size();i++){
            Chapter chapter=new Chapter();
            Element element=elements.get(i);

            chapter.setChapterUrl(BASEURL+element.attr("href"));
            chapter.setChapterName(element.text());
            chapter.setChapterId(i+1);
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
}
