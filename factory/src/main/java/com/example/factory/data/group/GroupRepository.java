package com.example.factory.data.group;

import android.text.TextUtils;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Group_Table;
import com.example.factory.model.sample.MemberUserModel;
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
        if (group.getGroupMemberCount() > 0) {
            group.holder = buildGroupHolder(group);
        } else {
            group.holder = null;
            GroupHelper.refreshGroupMembers(group.getId());
        }
        return true;
    }

    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    /**
     * 构建群成员信息
     * @param group 群
     * @return 群成员的前4个成员名称
     */
    private String buildGroupHolder(Group group) {
        List<MemberUserModel> sampleData = group.getLatelyGroupMembers();
        if (sampleData == null || sampleData.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (MemberUserModel model : sampleData) {
            builder.append(TextUtils.isEmpty(model.alias) ? model.name : model.alias);
            builder.append(", ");
        }
        builder.delete(builder.lastIndexOf(", "), builder.length());
        return builder.toString();
    }
}
