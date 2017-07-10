package com.example.factory.presenter.account;

import com.example.commom.factory.presenter.BaseContract;

/**
 * 注册契约类
 * Created by wenjian on 2017/6/12.
 */

public interface RegisterContract {

    interface View extends BaseContract.View<Presenter>{

        void registerSuccess();

    }


    interface Presenter extends BaseContract.Presenter{

        void register(String phone,String name,String password);

        boolean checkMobile(String phone);
    }
}
