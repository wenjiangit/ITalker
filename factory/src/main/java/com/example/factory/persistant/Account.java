package com.example.factory.persistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.factory.Factory;
import com.example.factory.model.api.AccountRspModel;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * 账户持久化信息
 * Created by douliu on 2017/6/16.
 */

public class Account {

    private static final String KEY_PUSH_ID = "key_push_id";
    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_ACCOUNT = "key_account";
    private static final String KEY_IS_BIND = "key_is_bind";

    private static String pushId;
    private static String token;
    private static String userId;
    private static String account;
    private static boolean isBind;

    public static void save(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName()
                ,Context.MODE_PRIVATE);
        sp.edit().putString(KEY_PUSH_ID, pushId)
                .putString(KEY_TOKEN,token)
                .putString(KEY_USER_ID,userId)
                .putString(KEY_ACCOUNT,account)
                .putBoolean(KEY_IS_BIND,isBind)
                .apply();
    }

    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName()
                ,Context.MODE_PRIVATE);
        pushId = sp.getString(KEY_PUSH_ID, "");
        token = sp.getString(KEY_TOKEN, "");
        userId = sp.getString(KEY_USER_ID, "");
        account = sp.getString(KEY_ACCOUNT, "");
        isBind = sp.getBoolean(KEY_IS_BIND, false);
    }

    public static boolean isBind() {
        return isBind;
    }

    public static void setBind(boolean isBind) {
        Account.isBind = isBind;
        save(Factory.app());
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(userId)&&
                !TextUtils.isEmpty(token);
    }

    public static String getPushId() {
        return pushId;
    }

    public static String getToken() {
        return token;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getAccount() {
        return account;
    }

    /**
     * 个人信息是否完善
     * @return
     */
    public static boolean isComplete() {
        if (isLogin()) {
            User self = getUser();
            return !TextUtils.isEmpty(self.getPortrait()) &&
                    !TextUtils.isEmpty(self.getDesc())
                    && self.getSex() != 0;
        }
        return false;
    }

    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        save(Factory.app());
    }

    /**
     * 保存个人信息
     *
     * @param model AccountRspModel
     */
    public static void login(AccountRspModel model) {
        token = model.getToken();
        userId = model.getUser().getId();
        account = model.getAccount();
        isBind = model.isBind();
        save(Factory.app());
    }

    /**
     * 从数据库获取个人信息
     *
     * @return User
     */
    public static User getUser() {
        return TextUtils.isEmpty(userId)?new User():
                SQLite.select().from(User.class)
                        .where(User_Table.id.eq(userId))
                        .querySingle();
    }
}
