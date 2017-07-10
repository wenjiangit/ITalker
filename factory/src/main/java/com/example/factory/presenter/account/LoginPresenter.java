package com.example.factory.presenter.account;

import android.text.TextUtils;
import android.util.Log;

import com.example.commom.factory.data.DataSource;
import com.example.commom.factory.presenter.BasePresenter;
import com.example.factory.R;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.api.LoginModel;
import com.example.factory.model.db.User;
import com.example.factory.persistant.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;


/**
 * 登录的Presenter
 *
 * Created by douliu on 2017/6/13.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter, DataSource.Callback<User> {

    private static final String TAG = "LoginPresenter";

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

        start();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            getView().showError(R.string.data_account_login_invalid_parameter);
        } else if (phone.length() < 11 || password.length() < 6) {
            getView().showError(R.string.data_account_login_error_validate);
        } else {
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(model, this);
        }

    }

    @Override
    public void onDataLoaded(User response) {
        Log.d(TAG, "onDataLoaded: " + response);
        final LoginContract.View view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });


    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final LoginContract.View view = getView();
        if (view == null) {
            return;
        }

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });

    }
}
