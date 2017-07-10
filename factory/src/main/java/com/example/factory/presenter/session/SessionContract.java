package com.example.factory.presenter.session;

import com.example.commom.factory.presenter.BaseContract;
import com.example.factory.model.db.Session;

/**
 * 会话契约
 * Created by wenjian on 2017/7/8.
 */

public interface SessionContract {


    interface Presenter extends BaseContract.Presenter{

    }

    interface View extends BaseContract.RecyclerView<Presenter, Session> {
    }



}
