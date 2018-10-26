package com.kalbe.kalbecallplanaedp.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.kalbe.kalbecallplanaedp.Common.tInfoProgramDetail;
import com.kalbe.kalbecallplanaedp.Common.tInfoProgramHeader;
import com.kalbe.kalbecallplanaedp.Data.DatabaseHelper;
import com.kalbe.kalbecallplanaedp.Data.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dewi Oktaviani on 10/24/2018.
 */

public class tInfoProgramDetailRepo implements crud {
    DatabaseHelper helper;

    public tInfoProgramDetailRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) throws SQLException {
        int index = -1;
        tInfoProgramDetail object = (tInfoProgramDetail) item;
        try {
            index = helper.gettInfoProgramDetailDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) throws SQLException {
        int index = -1;
        tInfoProgramDetail object = (tInfoProgramDetail) item;
        try {
            Dao.CreateOrUpdateStatus status  = helper.gettInfoProgramDetailDao().createOrUpdate(object);
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
        tInfoProgramDetail object = (tInfoProgramDetail) item;
        try {
            index = helper.gettInfoProgramDetailDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        tInfoProgramDetail item = null;
        try{
            item = helper.gettInfoProgramDetailDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<tInfoProgramDetail> items = null;
        try{
            items = helper.gettInfoProgramDetailDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public tInfoProgramDetail findByDetailId(String txtDetailId) throws SQLException {
        tInfoProgramDetail item = new tInfoProgramDetail();
        QueryBuilder<tInfoProgramDetail, Integer> queryBuilder = null;
        try {
            queryBuilder = helper.gettInfoProgramDetailDao().queryBuilder();
            queryBuilder.where().eq(item.Property_txtDetailId, txtDetailId);
            item = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public List<tInfoProgramDetail> findByHeaderId(String intHeaderId) throws SQLException {
        tInfoProgramDetail item = new tInfoProgramDetail();
        List<tInfoProgramDetail> listData = new ArrayList<>();
        QueryBuilder<tInfoProgramDetail, Integer> queryBuilder = null;
        try {
            queryBuilder = helper.gettInfoProgramDetailDao().queryBuilder();
            queryBuilder.where().eq(item.Property_txtHeaderId, intHeaderId);
            listData = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    public List<tInfoProgramDetail> getPushAllData(List<tInfoProgramHeader> headerList){
        List<String> headerIdList = new ArrayList<>();
        List<tInfoProgramDetail> detailList = new ArrayList<>();
        tInfoProgramDetail item = new tInfoProgramDetail();
        QueryBuilder<tInfoProgramDetail, Integer> queryBuilder = null;
        try {
            if (headerIdList!=null){
                if (headerList.size()>0){
                    for (tInfoProgramHeader header : headerList){
                        headerIdList.add(header.getTxtHeaderId());
                    }
                    queryBuilder = helper.gettInfoProgramDetailDao().queryBuilder();
                    queryBuilder.where().in(item.Property_txtHeaderId, headerIdList);
                    detailList = queryBuilder.query();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detailList;
    }
}