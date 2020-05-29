package com.huangshangi.novelreader.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SearchHistory {

    @Id
    private String searchContent;//搜索内容

    private String searchTime;//搜索时间

    @Generated(hash = 1464645733)
    public SearchHistory(String searchContent, String searchTime) {
        this.searchContent = searchContent;
        this.searchTime = searchTime;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }



    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }
}
