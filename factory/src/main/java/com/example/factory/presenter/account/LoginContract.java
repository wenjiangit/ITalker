package com.example.factory.presenter.account;

import com.example.commom.factory.presenter.BaseContract;

/**
 *
 * Created by douliu on 2017/6/13.
 */

public interface LoginContract {


    interface View extends BaseContract.View<Presenter> {

        void loginSuccess();
    }



    interface Presenter extends BaseContract.Presenter{

        void login(String phone, String password);




    }
}
