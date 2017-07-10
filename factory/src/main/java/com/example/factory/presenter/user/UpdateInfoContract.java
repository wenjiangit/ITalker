package com.example.factory.presenter.user;

import com.example.commom.factory.presenter.BaseContract;

/**
 *
 * Created by wenjian on 2017/6/18.
 */

public interface UpdateInfoContract {

    interface Presenter extends BaseContract.Presenter{

        void update(String portraitFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {

        void updateSucceed();
    }

}
