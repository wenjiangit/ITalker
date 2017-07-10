package com.example.factory.data.user;

import android.text.TextUtils;

import com.example.factory.data.helper.DbHelper;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * Created by douliu on 2017/6/27.
 */

public class UserDispatcher implements UserCenter{

    private static UserDispatcher instance;

    //维护一个单线程池进行统一的线程调度
    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static UserDispatcher instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null) {
                    instance = new UserDispatcher();
                }
            }
        }
        return instance;
    }

    private UserDispatcher(){}

    @Override
    public void dispatch(UserCard... userCards) {
        if (userCards == null || userCards.length == 0) {
            return;
        }

        //把数据扔进线程进行异步处理
        executor.execute(new UserCardHandler(userCards));

    }

    static class UserCardHandler implements Runnable {

        private UserCard[] mCards;

        UserCardHandler(UserCard[] cards) {
            mCards = cards;
        }

        @Override
        public void run() {
            List<User> users = new ArrayList<>();
            for (UserCard card : mCards) {
                if (card == null || TextUtils.isEmpty(card.getId())) {
                    continue;
                }
                users.add(card.buildUser());
            }

            //进行数据库的保存
            DbHelper.save(User.class, users.toArray(new User[0]));
        }
    }

}
