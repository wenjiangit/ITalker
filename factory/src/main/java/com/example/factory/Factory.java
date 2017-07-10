package com.example.factory;


import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.annotation.StringRes;
import android.util.Log;

import com.example.commom.app.Application;
import com.example.commom.factory.data.DataSource;
import com.example.factory.data.group.GroupCenter;
import com.example.factory.data.group.GroupDispatcher;
import com.example.factory.data.message.MessageCenter;
import com.example.factory.data.message.MessageDispatcher;
import com.example.factory.data.user.UserCenter;
import com.example.factory.data.user.UserDispatcher;
import com.example.factory.model.api.PushModel;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.GroupMember;
import com.example.factory.model.db.Message;
import com.example.factory.persistant.Account;
import com.example.factory.utils.DbflowExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wenjian on 2017/6/9.
 */

public class Factory {

    private static final String TAG = "Factory";


    private final Executor executor;

    private static final Factory instance;

    private final Gson gson;

    static {
        instance = new Factory();
    }

    private Factory() {
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")//将date类型转化成对应的字符串格式
                .setExclusionStrategies(new DbflowExclusionStrategy())//设置dbflow字段的过滤器
                .create();
    }

    public static void setup() {
        //将xml持久化数据读取到缓存
        Account.load(app());
        //数据库的初始化
        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true)//在数据库初始化的时候打开
                .build());

    }

    public static Application app() {
        return Application.getInstance();
    }

    public static void runOnBackground(Runnable runnable) {
        instance.executor.execute(runnable);
    }

    public static Gson getGson() {
        return instance.gson;
    }

    /**
     * 解析返回的错误码
     *
     * @param model    RspModel
     * @param callback 失败的回调
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null) {
            return;
        }

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    public static void logout() {
        FlowManager.destroy();
        SharedPreferences preferences = app().getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }

    /**
     * 处理错误码
     * @param resId 资源id
     * @param callback 回调
     */
    private static void decodeRspCode(@StringRes final int resId, final DataSource.FailedCallback callback) {
        if (callback != null) {
            callback.onDataNotAvailable(resId);
        }
    }

    public static void dispatchMessage(String str) {
        PushModel model = PushModel.decode(str);
        if (model == null) return;

        for (PushModel.Entity entity : model.getEntities()) {
            Log.i(TAG, "dispatchMessage: " + entity);
        }

        for (PushModel.Entity entity : model.getEntities()) {
            switch (entity.type) {
                case PushModel.ENTITY_TYPE_LOGOUT://退出登录
                    logout();
                    return;
                case PushModel.ENTITY_TYPE_MESSAGE://收到消息
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMessageCenter().dispatch(card);
                    break;
                case PushModel.ENTITY_TYPE_ADD_GROUP://添加群
                    GroupCard groupCard = getGson().fromJson(entity.content, GroupCard.class);
                    getGroupCenter().dispatch(groupCard);
                    break;
                case PushModel.ENTITY_TYPE_ADD_FRIEND://添加好友
                    UserCard userCard = getGson().fromJson(entity.content, UserCard.class);
                    getUserCenter().dispatch(userCard);
                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS://添加群成员
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS://群成员有变更
                    // 群成员变更, 回来的是一个群成员的列表
                    Type type = new TypeToken<List<GroupMemberCard>>() {
                    }.getType();
                    List<GroupMemberCard> cards = getGson().fromJson(entity.content, type);
                    // 把数据集合丢到数据中心处理
                    getGroupCenter().dispatch(cards.toArray(new GroupMemberCard[0]));

                    break;

                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS:
                    // TODO: 2017/7/2 退出通知

                    break;

            }
        }
    }

    /**
     * 获取用户数据调度中心
     * @return UserDispatcher
     */
    public static UserCenter getUserCenter() {
        return UserDispatcher.instance();
    }

    /**
     * 获取消息调度中心
     * @return MessageDispatcher
     */
    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.instance();
    }

     /**
     * 获取群组调度中心
     * @return MessageDispatcher
     */
    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.instance();
    }



}
