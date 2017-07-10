package com.example.factory.presenter.search;

import com.example.commom.factory.presenter.BaseContract;
import com.example.factory.model.db.User;

/**
 * 联系人列表拉取契约
 * Created by wenjian on 2017/6/24.
 */

public interface ContactContract {

    interface Presenter extends BaseContract.Presenter {
        //start中进行加载数据

    }


    interface View extends BaseContract.RecyclerView<Presenter, User> {
        //基类中已经全部有了
    }



}
