package com.kalbe.kalbecallplanaedp.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kalbe.kalbecallplanaedp.Common.mSubSubActivity;
import com.kalbe.kalbecallplanaedp.Data.DatabaseHelper;
import com.kalbe.kalbecallplanaedp.Data.DatabaseManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dewi Oktaviani on 10/15/2018.
 */

public class mSubSubActivityRepo implements crud{
    DatabaseHelper helper;

    public mSubSubActivityRepo(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) throws SQLException {
        int index = -1;
        mSubSubActivity object = (mSubSubActivity) item;
        try {
            index = helper.getmSubSubActivityDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) throws SQLException {
        int index = -1;
        mSubSubActivity object = (mSubSubActivity) item;
        try {
            Dao.CreateOrUpdateStatus status  = helper.getmSubSubActivityDao().createOrUpdate(object);
            index = status.getNumLinesChanged();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Object item) throws SQLException {
        int index = -1;
        mSubSubActivity object = (mSubSubActivity) item;
        try {
            index = helper.getmSubSubActivityDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        mSubSubActivity item = null;
        try{
            item = helper.getmSubSubActivityDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<mSubSubActivity> items = null;
        try{
            items = helper.getmSubSubActivityDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
