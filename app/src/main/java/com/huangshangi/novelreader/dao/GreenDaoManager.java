package com.huangshangi.novelreader.dao;


import com.huangshangi.novelreader.MyApplication;
import com.huangshangi.novelreader.dao.greendao.DaoMaster;
import com.huangshangi.novelreader.dao.greendao.DaoSession;

public class GreenDaoManager {

    private static GreenDaoManager instance;
    private static DaoMaster daoMaster;

    public static GreenDaoManager getInstance() {
        if (instance == null) {
            instance = new GreenDaoManager();
        }
        return instance;
    }

    private GreenDaoManager(){
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(MyApplication.getApplication(), "novelRead.db", null);
        daoMaster = new DaoMaster(openHelper.getWritableDb());

    }



    public DaoSession getSession(){
        return daoMaster.newSession();
    }
}

