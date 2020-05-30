package com.huangshangi.novelreader.crawler;

/*
    源：笔趣阁
    源地址：https://www.52bqg.com/
 */

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
    源地址：https://www.52bqg.com/
 */
public class BQG52Crawler implements BaseCrawler {

    final String BASEURL="https://www.52bqg.com/";

    static BQG52Crawler instance;

    public static BaseCrawler getInstance() {

        if(instance==null)
            if(instance==null)
                instance=new BQG52Crawler();
        return instance;
    }

    @Override
    public List<BookType> getBookTypeList(String html) {
        List<BookType>list=new ArrayList<>();

        Document document=Jsoup.parse(html);

        Elements elements=document.select("div.nav ul li a");


        for(int i=0;i<elements.size()-2;i++){
            Element element=elements.get(i);
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

        Document document=Jsoup.parse(html);
        Element elementType=document.selectFirst("div.l h2");
        Elements elements=document.select("div.l ul li");

        for(Element element:elements){
            Book book=new Book();

            book.setBookType(elementType.text());
            book.setBookIntroUrl(element.selectFirst("span.s2 a").attr("href"));
            book.setBookName(element.selectFirst("span.s2 a").text());

            list.add(book);
        }

        return list;

    }

    @Override
    public Book getBookIntro(String html) {

        Book book=new Book();

        Document document= Jsoup.parse(html);
        Element imageUrlElement=document.selectFirst("#fmimg img");//https://www.52bqg.com/files/article/image/127/127071/127071s.jpg
        Element bookNameElement=document.selectFirst("#info h1");
        Element authorElement=document.selectFirst("#info p a");
        Element updateDateElement=document.select("#info p").get(2);
        Element lastestChapterElemetn=document.select("#info p").get(3).selectFirst("a");
        Element introElement=document.selectFirst("#intro");
        Element typeElement=document.selectFirst(".con_top");

        book.setBookImage(imageUrlElement.attr("src"));
        book.setBookName(bookNameElement.text());
        book.setAuthorName(authorElement.text());
        book.setUpdateDate(updateDateElement.text());//更新时间：2020-05-21 10:45 [共185万字]
        book.setLastestChapter(lastestChapterElemetn.text());
        book.setBookIntro(introElement.text());
        book.setBookType(typeElement.text());// > 历史小说 > 我的帝国无双
        book.setBookId(StringUtil.getHash(book.getBookName()+book.getAuthorName()));
        return book;
    }

    @Override
    public List<Chapter> getChapters(String bookId,String html) {
        List<Chapter>list=new ArrayList<>();

        Document document=Jsoup.parse(html);

        Elements elements=document.select("#list dl dd");

        for(int i=0;i<elements.size();i++){
            Element element=elements.get(i);

            if(element==null)
                continue;

            Chapter chapter=new Chapter();
            chapter.setChapterUrl(element.baseUri()+element.select("a").attr("href"));
            chapter.setChapterName(element.select("a").text());
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
