package com.example.factory.presenter.user;

import com.example.commom.factory.presenter.BaseContract;
import com.example.factory.model.db.User;

/**
 *
 * Created by wenjian on 2017/6/24.
 */

public interface PersonalContract {

    interface Presenter extends BaseContract.Presenter{

        User getPersonal();

    }

    interface View extends BaseContract.View<Presenter> {

        void onLoadDone(User user);

        void allowSayHello(boolean isAllow);

        void setFollowState(boolean isFollow);

        String getUserId();
    }

}
