package com.example.factory.data.helper;

import android.util.Log;

import com.example.commom.factory.data.DataSource;
import com.example.commom.utils.CollectionUtil;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.UpdateInfoModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.example.factory.persistant.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by wenjian on 2017/6/18.
 */

public class UserHelper {

    private static final String TAG = "UserHelper";

    //更新用户信息
    public static void update(UpdateInfoModel model, final DataSource.Callback<UserCard> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                Log.i(TAG, "update onResponse: " + rspModel);
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    //将数据分发给UserCenter进行处理
                    Factory.getUserCenter().dispatch(userCard);
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                t.printStackTrace();
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }


    //搜索用户
    public static Call<RspModel<List<UserCard>>> search(String name, final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                Log.i(TAG, "search onResponse: " + rspModel);
                if (rspModel.success()) {
                    List<UserCard> userCards = rspModel.getResult();
                    callback.onDataLoaded(userCards);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                t.printStackTrace();
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return call;
    }


    //关注请求
    public static void follow(String userId, final DataSource.Callback<UserCard> callback) {
        Call<RspModel<UserCard>> call = Network.remote().userFollow(userId);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                Log.i(TAG, "follow onResponse: " + rspModel);
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    //将数据分发给UserCenter进行处理
                    Factory.getUserCenter().dispatch(userCard);
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                t.printStackTrace();
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    //刷新联系人列表
    public static void refreshContacts() {
        RemoteService service = Network.remote();
        Call<RspModel<List<UserCard>>> call = service.userContact();
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                Log.i(TAG, "refreshContacts onResponse: " + rspModel);
                if (rspModel.success()) {
                    List<UserCard> userCards = rspModel.getResult();
                    Factory.getUserCenter().dispatch(CollectionUtil.toArray(userCards,UserCard.class));
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static User findFromLocal(String userId) {
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }

    /**
     * 优先从本地拉取个人数据
     * @param userId 用户id
     * @return User
     */
    public static User search(String userId) {
        User user = findFromLocal(userId);
        if (user == null) {
            user = findFromNet(userId);
        }
        return user;
    }

    public static User findFromNet(String userId) {
        try {
            Call<RspModel<UserCard>> call = Network.remote().userFind(userId);
            Response<RspModel<UserCard>> response = call.execute();
            UserCard userCard = response.body().getResult();
            User user = userCard.buildUser();
            Factory.getUserCenter().dispatch(userCard);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 优先从网络拉取个人数据
     * @param userId 用户id
     * @return User
     */
    public static User searchFirstNet(String userId) {
        User user = findFromNet(userId);
        if (user == null) {
            user = findFromLocal(userId);
        }
        return user;
    }

    /**
     * 从本地数据库查询我的联系人列表
     * @return List<User>
     */
    public static List<User> contacts() {
        //查询本地数据库我的联系人列表
        return SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .queryList();
    }


}
