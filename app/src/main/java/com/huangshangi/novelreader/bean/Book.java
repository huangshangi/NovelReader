package com.huangshangi.novelreader.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String bookId;

    private String BookImage;//图书封面地址

    private String BookName;//图书名称

    private String AuthorName;//作者名称

    private String BookType;//图书类型

    private String BookIntro;//图书简介

    private int LeftToRead;//未读的章数

    private String updateDate;//最后更新时间

    private String lastestChapter;//最新章节

    private int historyChpater;//最后阅读的章节

    private String bookIntroUrl;//书的简介地址

    private String bookChapterUrl;//书的章节地址

    private String addShelfTime;//加入书架时间

    public Book() {
    }




    public Book(String bookId, String BookImage, String BookName, String AuthorName,
            String BookType, String BookIntro, int LeftToRead, String updateDate,
            String lastestChapter, int historyChpater, String bookIntroUrl,
            String bookChapterUrl) {
        this.bookId = bookId;
        this.BookImage = BookImage;
        this.BookName = BookName;
        this.AuthorName = AuthorName;
        this.BookType = BookType;
        this.BookIntro = BookIntro;
        this.LeftToRead = LeftToRead;
        this.updateDate = updateDate;
        this.lastestChapter = lastestChapter;
        this.historyChpater = historyChpater;
        this.bookIntroUrl = bookIntroUrl;
        this.bookChapterUrl = bookChapterUrl;
    }




    @Generated(hash = 60993796)
    public Book(String bookId, String BookImage, String BookName, String AuthorName,
            String BookType, String BookIntro, int LeftToRead, String updateDate,
            String lastestChapter, int historyChpater, String bookIntroUrl,
            String bookChapterUrl, String addShelfTime) {
        this.bookId = bookId;
        this.BookImage = BookImage;
        this.BookName = BookName;
        this.AuthorName = AuthorName;
        this.BookType = BookType;
        this.BookIntro = BookIntro;
        this.LeftToRead = LeftToRead;
        this.updateDate = updateDate;
        this.lastestChapter = lastestChapter;
        this.historyChpater = historyChpater;
        this.bookIntroUrl = bookIntroUrl;
        this.bookChapterUrl = bookChapterUrl;
        this.addShelfTime = addShelfTime;
    }



    public String getBookImage() {
        return BookImage;
    }

    public void setBookImage(String bookImage) {
        BookImage = bookImage;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getBookType() {
        return BookType;
    }

    public void setBookType(String bookType) {
        BookType = bookType;
    }

    public String getBookIntro() {
        return BookIntro;
    }

    public void setBookIntro(String bookIntro) {
        BookIntro = bookIntro;
    }


    public int getLeftToRead() {
        return LeftToRead;
    }

    public void setLeftToRead(int leftToRead) {
        LeftToRead = leftToRead;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getLastestChapter() {
        return lastestChapter;
    }

    public void setLastestChapter(String lastestChapter) {
        this.lastestChapter = lastestChapter;
    }

    public int getHistoryChpater() {
        return historyChpater;
    }

    public void setHistoryChpater(int historyChpater) {
        this.historyChpater = historyChpater;
    }

    public String getBookIntroUrl() {
        return bookIntroUrl;
    }

    public void setBookIntroUrl(String bookIntroUrl) {
        this.bookIntroUrl = bookIntroUrl;
    }

    public String getBookChapterUrl() {
        return bookChapterUrl;
    }

    public void setBookChapterUrl(String bookChapterUrl) {
        this.bookChapterUrl = bookChapterUrl;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getAddShelfTime() {
        return addShelfTime;
    }

    public void setAddShelfTime(String addShelfTime) {
        this.addShelfTime = addShelfTime;
    }
}
