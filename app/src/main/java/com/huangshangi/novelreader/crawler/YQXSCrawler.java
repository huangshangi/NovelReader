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

public class YQXSCrawler implements BaseCrawler {

    static String BASEURL="http://www.7xxs.net";

    @Override
    public List<BookType> getBookTypeList(String html) {

        Document document= Jsoup.parse(html);
        List<BookType>list=new ArrayList<>();
        Elements elements=document.select("#wrapper .nav ul li a");
        for(int i=0;i<7;i++){
            BookType bookType=new BookType();
            Element element=elements.get(i);
            String type=element.text();
            String link=BASEURL+element.attr("href");
            bookType.setTypeUrl(link);
            bookType.setTypeName(type);

            list.add(bookType);
        }

        return list;
    }

    @Override
    public List<Book> getBooksList(String html) {

        Document document=Jsoup.parse(html);

        Elements elements=document.select("div#newscontent .r ul li");

        List<Book>list=new ArrayList<>();
        for(Element element:elements){
            Book book=new Book();
            book.setBookName(element.selectFirst(".s2 a").text());
            book.setBookIntroUrl(BASEURL+element.selectFirst(".s2 a").attr("href"));
            book.setBookChapterUrl(book.getBookIntroUrl());
            book.setAuthorName(element.selectFirst(".s5 a").text());

            list.add(book);
        }
        return list;
    }

    @Override
    public Book getBookIntro(String html) {

        Book book=new Book();
        Document document=Jsoup.parse(html);

        book.setAuthorName(document.selectFirst("#info p").text());//<p>作　　者：秦二风</p>
        book.setBookName(document.selectFirst("#info .booktitle h1").text());
        book.setBookChapterUrl(BASEURL+document.selectFirst("#list dl dt a").attr("href"));
        book.setBookIntroUrl(book.getBookChapterUrl());
        book.setUpdateDate(document.selectFirst("#info p:nth-child(3)").text());//最后更新：2020-05-30 01:25:51
        book.setBookIntro(document.selectFirst("#intro").text());
        book.setLastestChapter(document.selectFirst("#info p:nth-child(4) a").text());
        book.setBookId(StringUtil.getHash(book.getBookName()+book.getAuthorName()));


        return book;
    }

    @Override
    public List<Chapter> getChapters(String bookId, String html) {

        Document document=Jsoup.parse(html);

        Elements elements=document.select("#list dl dt:nth-child(2) +dd a");

        List<Chapter>list=new ArrayList<>();
        for(int i=0;i<elements.size();i++){
            Element element=elements.get(i);
            Chapter chapter=new Chapter();
            chapter.setBookId(bookId);
            chapter.setChapterId(i+1);

            chapter.setChapterName(element.text());
            chapter.setChapterUrl(BASEURL+element.attr("href"));

            list.add(chapter);

        }
        return list;
    }

    @Override
    public String getContent(String html) {
        return Jsoup.parse(html).select("#content").text();
    }
}
