package com.example.factory.data.group;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Group;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 *
 * Created by douliu on 2017/7/19.
 */

public class GroupRepository extends BaseDbRepository<Group>
        implements GroupDataSource {
    @Override
    protected boolean isRequired(Group group) {
        return true;
    }

    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Group.class)
                .async()
                .queryListResultCallback(this)
                .execute();
    }
}
