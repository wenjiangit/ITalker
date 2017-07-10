package com.example.commom.factory.model;

import java.io.Serializable;

/**
 * 用户常用信息获取的接口
 * Created by wenjian on 2017/6/24.
 */

public interface Author extends Serializable{
    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    String getPortrait();

    void setPortrait(String portrait);

    String getDesc();

    void setDesc(String desc);
}
