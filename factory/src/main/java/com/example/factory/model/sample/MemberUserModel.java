package com.example.factory.model.sample;

import com.example.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

/**
 *
 * Created by wenjian on 2017/7/23.
 */
@QueryModel(database = AppDatabase.class)
public class MemberUserModel {

    @Column
    public String userId;
    @Column
    public String alias;
    @Column
    public String name;
    @Column
    public String portrait;
}
