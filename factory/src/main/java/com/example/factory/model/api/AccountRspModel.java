package com.example.factory.model.api;

import com.example.factory.model.db.User;

/**
 * 账户操作返回model
 *
 * Created by wenjian on 2017/6/14.
 */

public class AccountRspModel {

    private User userCard;
    private String account;
    private String token;
    private boolean isBind;

    public User getUser() {
        return userCard;
    }

    public void setUser(User user) {
        this.userCard = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    @Override
    public String toString() {
        return "AccountRspModel{" +
                "user=" + userCard +
                ", account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBind=" + isBind +
                '}';
    }
}
