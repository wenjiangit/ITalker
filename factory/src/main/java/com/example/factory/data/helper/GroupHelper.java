package com.example.factory.data.helper;

import android.util.Log;

import com.example.commom.factory.data.DataSource;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.GroupCreateModel;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.GroupMember;
import com.example.factory.model.db.GroupMember_Table;
import com.example.factory.model.db.Group_Table;
import com.example.factory.model.db.User;
import com.example.factory.model.db.User_Table;
import com.example.factory.model.sample.MemberUserModel;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
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

    /**
     * 查找一个群，现本地后网路
     * @param id 群id
     * @return Group
     */
    public static Group find(String id) {
        Group group = findFromLocal(id);
        if (group == null) {
            group = findFromNet(id);
        }
        return group;
    }

    /**
     * 同步的方式从网络中查询
     * @param id 群id
     * @return Group
     */
    public static Group findFromNet(String id) {
        Call<RspModel<GroupCard>> call = Network.remote().getGroup(id);
        try {
            Response<RspModel<GroupCard>> response = call.execute();
            RspModel<GroupCard> rspModel = response.body();
            if (rspModel != null && rspModel.success()) {
                GroupCard groupCard = rspModel.getResult();
                if (groupCard != null) {
                    //数据库更新存储
                    Factory.getGroupCenter().dispatch(groupCard);
                    User owner = UserHelper.search(groupCard.getOwnerId());
                    if (owner != null) {
                        return groupCard.build(owner);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    if (groupCards != null) {
                        Factory.getGroupCenter().dispatch(groupCards.toArray(new GroupCard[0]));
                    }
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

    //搜索用户
    public static Call<RspModel<List<GroupCard>>> search(String name, final DataSource.Callback<List<GroupCard>> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<List<GroupCard>>> call = service.groupSearch(name);
        call.enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call, Response<RspModel<List<GroupCard>>> response) {
                RspModel<List<GroupCard>> rspModel = response.body();
                Log.i(TAG, "search group: " + rspModel);
                if (rspModel.success()) {
                    List<GroupCard> userCards = rspModel.getResult();
                    callback.onDataLoaded(userCards);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
                t.printStackTrace();
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return call;
    }


    public static long getGroupMemberCount(String groupId) {
        return SQLite.selectCountOf()
                .from(GroupMember.class)
                .where(GroupMember_Table.group_id.eq(groupId))
                .count();
    }

    public static List<MemberUserModel> getLatelyGroupMembers(String id, int size) {
        return  SQLite.select(GroupMember_Table.alias.withTable().as("alias"),
                User_Table.id.withTable().as("userId"),
                User_Table.name.withTable().as("name"),
                User_Table.portrait.withTable().as("portrait"))
                .from(GroupMember.class)
                .join(User.class, Join.JoinType.INNER)
                .on(GroupMember_Table.user_id.withTable().eq(User_Table.id.withTable()))
                .where(GroupMember_Table.group_id.withTable().eq(id))
                .orderBy(GroupMember_Table.user_id, true)
                .limit(size)
                .queryCustomList(MemberUserModel.class);
    }

    public static void refreshGroupMembers(String id) {
        Call<RspModel<List<GroupMemberCard>>> call = Network.remote().groupMembers(id);
        call.enqueue(new Callback<RspModel<List<GroupMemberCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupMemberCard>>> call,
                                   Response<RspModel<List<GroupMemberCard>>> response) {
                RspModel<List<GroupMemberCard>> rspModel = response.body();
                Log.i(TAG, "refreshGroupMembers: " + rspModel);
                if (rspModel != null && rspModel.success()) {
                    List<GroupMemberCard> groupCards = rspModel.getResult();
                    if (groupCards != null) {
                        Factory.getGroupCenter().dispatch(groupCards.toArray(new GroupMemberCard[0]));
                    }
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupMemberCard>>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
