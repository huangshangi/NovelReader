package com.huangshangi.novelreader.dao.dbservice;

import android.database.Cursor;
import android.util.Log;

import com.huangshangi.novelreader.bean.Chapter;
import com.huangshangi.novelreader.dao.greendao.ChapterDao;

import java.util.ArrayList;
import java.util.List;

public class ChapterDBService extends BaseDBService {


    //缓存章节内容
    public void cacheChapter(String bookId,Chapter chapter){

        String id=bookId+chapter.getChapterId();

        chapter.setId(id);
        super.addEntity(chapter);
    }

    //根据id获取某章节内容
    public Chapter getChapterContentByKey(String bookId,int chapterCount){
        String id=bookId+chapterCount;

        String where="WHERE "+ ChapterDao.Properties.Id.columnName+"=? ;";

        List<Chapter>list=(List<Chapter>)getEntity(Chapter.class,where,new String[]{id});
        if(list.size()!=0)
            return list.get(0);

        return null;
    }

    //获取章节目录
    public List<Chapter>getChaptersMenu(String bookId){

        String where=" WHERE "+ ChapterDao.Properties.BookId.columnName+"=? order by "+ChapterDao.Properties.ChapterId.columnName;

        List<Chapter>list=(List<Chapter>)getEntity(Chapter.class,where,new String[]{bookId});

        return list;
    }


    //储存章节目录
    public void addCacheChaptersMenu(List<Chapter>list){

        for(Chapter chapter:list){

            addEntity(chapter);
        }




    }

    //更新某一章节内容
    public void updateCacheChapter(Chapter chapter){

        updateEntity(chapter);
    }

}
