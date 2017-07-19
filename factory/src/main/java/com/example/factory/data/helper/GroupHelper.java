package com.example.factory.data.helper;

import android.util.Log;

import com.example.commom.factory.data.DataSource;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.GroupCreateModel;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Group_Table;
import com.example.factory.net.Network;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by douliu on 2017/6/27.
 */

public class GroupHelper {

    private static final String TAG = "GroupHelper";
    /**
     * 从本地查找群
     * @param id 群id
     * @return 群信息
     */
    public static Group findFromLocal(String id) {
        return SQLite.select()
                .from(Group.class)
                .where(Group_Table.id.eq(id))
                .querySingle();
    }


    public static Group find(String id) {
        // TODO: 2017/6/28 先本地后网络
        return null;
    }

    /**
     * 创建群
     * @param model 创建群信息
     * @param callback 回调
     */
    public static void create(GroupCreateModel model, final DataSource.Callback<GroupCard> callback) {
        Call<RspModel<GroupCard>> call = Network.remote().groupCreate(model);
        call.enqueue(new Callback<RspModel<GroupCard>>() {
            @Override
            public void onResponse(Call<RspModel<GroupCard>> call, Response<RspModel<GroupCard>> response) {
                RspModel<GroupCard> rspModel = response.body();
                Log.i(TAG, "create: " + rspModel);
                if (rspModel != null && rspModel.success()) {
                    GroupCard groupCard = rspModel.getResult();
                    Factory.getGroupCenter().dispatch(groupCard);
                    callback.onDataLoaded(groupCard);
                } else {
                    Factory.decodeRspCode(rspModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<GroupCard>> call, Throwable t) {
                t.printStackTrace();
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 搜索群，通过日期,获取相应日期以后有过变更的群
     *
     * @param dateStr 日期字符串{@link System#currentTimeMillis()}
     */
    public static void list(String dateStr) {
        Call<RspModel<List<GroupCard>>> call = Network.remote().groupList(dateStr);
        call.enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call,
                                   Response<RspModel<List<GroupCard>>> response) {
                RspModel<List<GroupCard>> rspModel = response.body();
                Log.i(TAG, "list: " + rspModel);
                if (rspModel != null && rspModel.success()) {
                    List<GroupCard> groupCards = rspModel.getResult();
                    Factory.getGroupCenter().dispatch(groupCards.toArray(new GroupCard[0]));
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
