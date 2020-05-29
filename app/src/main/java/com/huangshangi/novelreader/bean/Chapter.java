package com.huangshangi.novelreader.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class Chapter {

    @Id
    private String id;

    private int chapterId;//章节编号

    private String bookId;//所属书的id

    private String chapterName;//章节名称

    @Transient
    private String chapterUrl;//章节内容url

    private String chapterContent;//章节内容


   


    public Chapter() {
    }



    public Chapter(String id, int chapterId, String chapterName,
            String chapterContent) {
        this.id = id;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.chapterContent = chapterContent;
    }



    @Generated(hash = 1759523728)
    public Chapter(String id, int chapterId, String bookId, String chapterName,
            String chapterContent) {
        this.id = id;
        this.chapterId = chapterId;
        this.bookId = bookId;
        this.chapterName = chapterName;
        this.chapterContent = chapterContent;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public String getChapterContent() {
        return chapterContent;
    }

    public void setChapterContent(String chapterContent) {
        this.chapterContent = chapterContent;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
