package com.example.factory.model.db;

import com.example.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 基于数据库框架dbflow的基本model
 * Created by wenjian on 2017/7/2.
 */

public abstract class BaseDbModel<Model> extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Model>{
}
