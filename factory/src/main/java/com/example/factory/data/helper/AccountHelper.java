package com.example.factory.data.helper;

import android.text.TextUtils;
import android.util.Log;

import com.example.commom.factory.data.DataSource;
import com.example.commom.net.model.RegisterModel;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.AccountRspModel;
import com.example.factory.model.api.LoginModel;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.db.User;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.example.factory.persistant.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 账户帮助类
 *
 * Created by douliu on 2017/6/13.
 */

public class AccountHelper {

    private static final String TAG = "AccountHelper";

    /**
     * 注册
     * @param model RegisterModel
     * @param callback 回调
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
        Call<RspModel<AccountRspModel>> call = Network.remote().accountRegister(model);
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 处理返回结果
     * @param response Response
     * @param callback 回调
     */
    private static void processResponse(Response<RspModel<AccountRspModel>> response, DataSource.Callback<User> callback) {
        RspModel<AccountRspModel> rspModel = response.body();
        Log.i(TAG, "processResponse: "+rspModel);
        if (rspModel.success()) {
            AccountRspModel accountRspModel = rspModel.getResult();
            User user = accountRspModel.getUser();
            //数据库保存
            DbHelper.save(User.class, user);
            //自己信息做一个xml持久化
            Account.login(accountRspModel);
            if (accountRspModel.isBind()) {
                Account.setBind(true);
                if (callback != null) {
                    callback.onDataLoaded(user);
                }
            } else {
                bindPush(callback);
            }
        } else {
            //统一处理错误信息
            Factory.decodeRspCode(rspModel, callback);
        }
    }

    /**
     * 进行pushId的绑定
     * @param callback 回调
     */
    public static void bindPush(DataSource.Callback<User> callback) {
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) {//如果还没有值，则不进行绑定
            return;
        }
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 登录
     * @param model 登录的model
     * @param callback 回调
     */
    public static void login(LoginModel model, final DataSource.Callback<User> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        call.enqueue(new AccountRspCallback(callback));
    }


    /**
     * 对账户操作返回进行统一处理
     */
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>>{
        DataSource.Callback<User> callback;

        AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call,
                               Response<RspModel<AccountRspModel>> response) {
            processResponse(response, callback);
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            t.printStackTrace();
            if (callback != null) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        }
    }
}
