package com.example.commom.factory.data;

import java.util.List;

/**
 *
 * Created by wenjian on 2017/7/2.
 */

public interface DbDataSource<Model> extends DataSource {

    void load(DataSource.SucceedCallback<List<Model>> callback);


}
