package com.example.factory.presenter.account;

import android.text.TextUtils;

import com.example.commom.Common;
import com.example.commom.factory.data.DataSource;
import com.example.commom.factory.presenter.BasePresenter;
import com.example.commom.net.model.RegisterModel;
import com.example.factory.R;
import com.example.factory.data.helper.AccountHelper;
import com.example.factory.model.db.User;
import com.example.factory.persistant.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/**
 *
 * Created by wenjian on 2017/6/12.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {

    public RegisterPresenter(RegisterContract.View view) {
        super(view);

    }

    @Override
    public void register(String phone, String name, String password) {

        start();

        RegisterContract.View view = getView();

        if (!checkMobile(phone)) {
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length()<2) {
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            RegisterModel model = new RegisterModel(phone, name, password, Account.getPushId());
            AccountHelper.register(model,this);
        }

    }

    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone) &&
                Pattern.matches(Common.Constants.REGEX_MOBILE, phone);
    }

    @Override
    public void onDataLoaded(User user) {
        final RegisterContract.View view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final RegisterContract.View view = getView();
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
