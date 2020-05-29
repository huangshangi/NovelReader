package com.huangshangi.novelreader.dao.dbservice;

import android.database.Cursor;
import android.util.Log;

import com.huangshangi.novelreader.dao.GreenDaoManager;
import com.huangshangi.novelreader.dao.greendao.DaoSession;

import java.util.List;

public  class BaseDBService {

    public void addEntity(Object object){
        DaoSession daoSession= GreenDaoManager.getInstance().getSession();
        daoSession.insert(object);


    }



    public void deleteEntity(Object object){
        GreenDaoManager.getInstance().getSession().delete(object);
    }


    void updateEntity(Object object){
        GreenDaoManager.getInstance().getSession().update(object);

    }

    List<?> getEntity(Class c, String where, String[]selectArgs){
       return  GreenDaoManager.getInstance().getSession().queryRaw(c,where,selectArgs);
    }

    Cursor selectListBySql(String sql,String[]selectArgs){
        return GreenDaoManager.getInstance().getSession().getDatabase().rawQuery(sql,selectArgs);
    }

    public void ADUListBySql(String sql,String[]selectArgs){


        GreenDaoManager.getInstance().getSession().getDatabase().rawQuery(sql,selectArgs);
    }

    public void deleteAll(Class obj){
        GreenDaoManager.getInstance().getSession().deleteAll(obj);
    }
}
