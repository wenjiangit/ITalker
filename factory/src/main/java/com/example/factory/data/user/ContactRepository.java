package com.example.factory.data.user;

import com.example.commom.factory.data.DataSource;
import com.example.factory.data.BaseDbRepository;
import com.example.factory.data.helper.DbHelper;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.persistant.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.List;

/**
 *
 * Created by wenjian on 2017/7/2.
 */

public class ContactRepository extends BaseDbRepository<User> implements
        ContactDataSource,
        QueryTransaction.QueryResultListCallback<User>,
        DbHelper.ChangeListener<User>{
    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        super.load(callback);
        //查询本地数据库我的联系人列表
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equalsIgnoreCase(Account.getUserId());
    }


}
