package com.kalbe.kalbecallplanaedp.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.kalbe.kalbecallplanaedp.Common.tProgramVisitSubActivityAttachment;
import com.kalbe.kalbecallplanaedp.Data.DatabaseHelper;
import com.kalbe.kalbecallplanaedp.Data.DatabaseManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dewi Oktaviani on 10/19/2018.
 */

public class tProgramVisitSubActivityAttachmentRepo implements crud {
    DatabaseHelper helper;

    public tProgramVisitSubActivityAttachmentRepo(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) throws SQLException {
        int index = -1;
        tProgramVisitSubActivityAttachment object = (tProgramVisitSubActivityAttachment) item;
        try {
            index = helper.gettProgramVisitSubActivityAttachmentDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) throws SQLException {
        int index = -1;
        tProgramVisitSubActivityAttachment object = (tProgramVisitSubActivityAttachment) item;
        try {
            Dao.CreateOrUpdateStatus status  = helper.gettProgramVisitSubActivityAttachmentDao().createOrUpdate(object);
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
        tProgramVisitSubActivityAttachment object = (tProgramVisitSubActivityAttachment) item;
        try {
            index = helper.gettProgramVisitSubActivityAttachmentDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        tProgramVisitSubActivityAttachment item = null;
        try{
            item = helper.gettProgramVisitSubActivityAttachmentDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<tProgramVisitSubActivityAttachment> items = null;
        try{
            items = helper.gettProgramVisitSubActivityAttachmentDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public tProgramVisitSubActivityAttachment findBytxtId(String txtId) throws SQLException {
        tProgramVisitSubActivityAttachment item = new tProgramVisitSubActivityAttachment();
        QueryBuilder<tProgramVisitSubActivityAttachment, Integer> queryBuilder = null;
        try {
            queryBuilder = helper.gettProgramVisitSubActivityAttachmentDao().queryBuilder();
            queryBuilder.where().eq(item.Property_txtProgramVisitSubActivityAttachmentId, txtId);
            item = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }
}
