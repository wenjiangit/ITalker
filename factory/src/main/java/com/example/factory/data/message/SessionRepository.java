package com.example.factory.data.message;

import android.support.annotation.NonNull;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Session;
import com.example.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

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
        mDataList.addFirst(session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {

        //反转
//        Collections.reverse(mDataList);
        super.onListQueryResult(transaction, tResult);

    }
}
