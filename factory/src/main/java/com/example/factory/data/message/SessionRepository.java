package com.example.factory.data.message;

import android.support.annotation.NonNull;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Session;
import com.example.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 *
 * Created by wenjian on 2017/7/9.
 */

public class SessionRepository extends BaseDbRepository<Session>
        implements SessionDataSource {

    @Override
    public void load(SucceedCallback<List<Session>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Session.class)
                .orderBy(Session_Table.modifyAt, false)
                .async()
                .queryListResultCallback(this)
                .execute();

    }

    @Override
    protected boolean isRequired(Session session) {
        return true;
    }

    @Override
    protected void insert(Session session) {
        //添加数据到第一个
//        super.insert(session);
        mDataList.addFirst(session);
    }

    @Override
    protected void replace(int index, Session session) {
//        super.replace(index, session);
        //replace的时候将Session添加到第一位
        mDataList.remove(index);
        mDataList.add(0, session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {

        //反转
        Collections.reverse(mDataList);
        super.onListQueryResult(transaction, tResult);

    }
}
