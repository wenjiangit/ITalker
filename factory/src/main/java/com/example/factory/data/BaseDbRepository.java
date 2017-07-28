package com.example.factory.data;

import android.support.annotation.NonNull;

import com.example.commom.factory.data.DbDataSource;
import com.example.factory.data.helper.DbHelper;
import com.example.factory.model.db.BaseDbModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * 基础的数据库仓库
 * Created by wenjian on 2017/7/2.
 */

public abstract class BaseDbRepository<Data extends BaseDbModel<Data>>
        implements DbDataSource<Data>,
        QueryTransaction.QueryResultListCallback<Data>,
        DbHelper.ChangeListener<Data> {

    //数据加载成功的回调,由presenter提供
    private SucceedCallback<List<Data>> mCallback;

    //数据库数据缓存
    protected final LinkedList<Data> mDataList = new LinkedList<>();

    //数据类型,即对应的class对象
    private Class<Data> mDataClass;

    @SuppressWarnings("unchecked")
    public BaseDbRepository() {
        Type[] types = Reflector.getActualTypeArguments(BaseDbRepository.class, this.getClass());
        mDataClass = (Class<Data>) types[0];
    }

    @Override
    public void load(SucceedCallback<List<Data>> callback) {
        this.mCallback = callback;
        //注册数据库变更监听
        DbHelper.addChangeListener(mDataClass, this);
    }

    @Override
    public void dispose() {
        this.mCallback = null;
        //注销数据库监听
        DbHelper.removeChangeListener(mDataClass, this);
        //清空缓存
        mDataList.clear();
    }

    @Override
    public void onDataSave(Data[] list) {
        boolean changed = false;
        for (Data data : list) {
            if (isRequired(data)) {
                insertOrUpdate(data);
                changed = true;
            }
        }

        if (changed) {
            notifyDataChanged();
        }
    }

    protected void insertOrUpdate(Data data) {
        int index = indexOf(data);
        if (index >= 0) {
            replace(index,data);
        } else {
            insert(data);
        }
    }

    //替换操作
    protected void replace(int index, Data data) {
        mDataList.remove(index);
        mDataList.add(index, data);
    }

    //插入操作
    protected void insert(Data data) {
        mDataList.add(data);
    }

    //从缓存中查询数据,有则返回坐标
    protected int indexOf(Data newData) {
        int index = -1;
        for (Data data : mDataList) {
            index++;
            if (data.isSame(newData)) {
                return index;
            }
        }
        return -1;
    }

    //过滤,需要子类实现
    protected abstract boolean isRequired(Data data);

    @Override
    public void onDataDelete(Data[] list) {
        boolean changed = false;
        for (Data data : list) {
            if (mDataList.remove(data)) {//移除成功,则说明数据有变更,需要通知刷新
                changed = true;
            }
        }

        if (changed) {
            notifyDataChanged();
        }
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        if (tResult.size() == 0) {
            mDataList.clear();
            notifyDataChanged();
            return;
        }

        /*Data[] users = CollectionUtil.toArray(tResult, mDataClass);
        onDataSave(users);*/
        mDataList.addAll(tResult);
        notifyDataChanged();
    }

    /**
     * 通知更新
     */
    private void notifyDataChanged() {
        if (mCallback != null) {
            mCallback.onDataLoaded(mDataList);
        }
    }
}
