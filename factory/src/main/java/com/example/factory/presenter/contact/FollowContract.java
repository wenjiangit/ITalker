package com.example.factory.presenter.contact;

import com.example.commom.factory.presenter.BaseContract;
import com.example.factory.model.card.UserCard;

/**
 * 关注的契约类
 * Created by wenjian on 2017/6/23.
 */

public interface FollowContract {

    interface Presenter extends BaseContract.Presenter{

        void follow(String userId);

    }

    interface View extends BaseContract.View<Presenter>{

        void onFollowSucceed(UserCard userCard);

    }
}
