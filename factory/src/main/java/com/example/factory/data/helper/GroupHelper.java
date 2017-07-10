package com.example.factory.data.helper;

import com.example.factory.model.card.GroupCard;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Group_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 *
 * Created by douliu on 2017/6/27.
 */

public class GroupHelper {
    /**
     * 从本地查找群
     * @param id 群id
     * @return 群信息
     */
    public static Group findFromLocal(String id) {
        return SQLite.select()
                .from(Group.class)
                .where(Group_Table.id.eq(id))
                .querySingle();
    }


    public static Group find(String id) {
        // TODO: 2017/6/28 先本地后网络
        return null;
    }
}
