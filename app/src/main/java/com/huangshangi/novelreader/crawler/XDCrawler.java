package com.huangshangi.novelreader.crawler;

import com.huangshangi.novelreader.bean.Book;
import com.huangshangi.novelreader.bean.BookType;
import com.huangshangi.novelreader.bean.Chapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.List;

public class XDCrawler implements BaseCrawler {

    static String BASEURL="http://www.xundu.net";
    @Override
    public List<BookType> getBookTypeList(String html) {

        Document document= Jsoup.parse(html);

        Elements elements=document.select(".clearfix .fl ul li a");
        List<BookType>list=new ArrayList<>();
        for(Element element:elements){
            BookType bookType=new BookType();
            bookType.setTypeName(element.attr("title"));
            bookType.setTypeUrl(BASEURL+element.attr("href"));

            list.add(bookType);
        }

        return list;
    }

    @Override
    public List<Book> getBooksList(String html) {

        Document document=Jsoup.parse(html);

        Elements elements=document.select(".pt-sort-detail .color7");

        List<Book>list=new ArrayList<>();

        for(int i=1;i<elements.size();i++){
            Element element=elements.get(i);
            Book book=new Book();
            book.setBookType(element.selectFirst("span:nth-child(1) a").text());

            book.setBookName(element.selectFirst("span:nth-child(2) a").text());
            book.setUpdateDate(element.selectFirst("span:nth-child(3) a").text());
            book.setAuthorName(element.selectFirst("span:nth-child(4) a").text());
            book.setUpdateDate(element.selectFirst("span:nth-child(5) a").text());//05-30 17:04
            list.add(book);
        }

        return list;
    }

    @Override
    public Book getBookIntro(String html) {

        Document document=Jsoup.parse(html);

        Book book=new Book();

        book.setBookName(document.selectFirst(".fl h1 a").text());
        book.setAuthorName(document.selectFirst(".fl span  a span").text());
        book.setBookType(document.selectFirst(".fl div span:nth-child(2) a span").text());
        book.setBookIntro(document.selectFirst(".compulsory-row-three  p").text());
        book.setLastestChapter(document.selectFirst("div.fl div-nth:child(4) a").attr("href"));
        book.setUpdateDate(document.selectFirst("div.fl div-nth:child(4) span").text());
        book.setBookIntroUrl(BASEURL+document.selectFirst("div.fl idv h1 a").attr("href"));
        book.setBookChapterUrl(book.getBookIntroUrl());

        return book;
    }

    @Override
    public List<Chapter> getChapters(String bookId, String html) {

        List<Chapter>list=new ArrayList<>();

        Document document=Jsoup.parse(html);

        Elements elements=document.select("div.full a");

        for(Element element:elements){
            Chapter chapter=new Chapter();
            chapter.setChapterUrl(BASEURL+element.attr("href"));
            chapter.setChapterName(element.attr("title"));
            chapter.setBookId(bookId);
            list.add(chapter);
        }

        return list;
    }

    @Override
    public String getContent(String html) {
        return Jsoup.parse(html).selectFirst("div.pt-read-cont .pt-read-text").text();
    }
}
