package com.huangshangi.novelreader.dao.dbservice;

import com.huangshangi.novelreader.bean.SearchHistory;
import com.huangshangi.novelreader.dao.greendao.SearchHistoryDao;

import java.util.List;

public class SearchHistoryDBService extends BaseDBService{


    //添加一条历史记录
    public void addSearchHistory(SearchHistory searchHistory){
        addEntity(searchHistory);
    }


    //获取所有历史记录
    public List<SearchHistory> getSearchHistorys(){

        List<SearchHistory>list=(List<SearchHistory>)getEntity(SearchHistory.class,"order by "+ SearchHistoryDao.Properties.SearchTime.columnName,null);
        return list;

    }


    //清空所有历史记录
    public void deleteSeachHistorys(){

        deleteAll(SearchHistory.class);

    }


    //添加历史记录
    public void addSearchHistorys(List<SearchHistory>list){

        for(SearchHistory searchHistory:list)
            addEntity(searchHistory);


    }
}
