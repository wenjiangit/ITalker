package com.example.factory.data.helper;

import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.GroupMember;
import com.example.factory.model.db.Group_Table;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Session;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库的辅助类
 * 封装数据库的增删改操作
 * Created by douliu on 2017/6/27.
 */

public class DbHelper {

    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    private DbHelper() {
    }

    /**
     * 监听器的集合
     */
    private static final Map<Class<?>, Set<ChangeListener>> mChangeListeners = new Hashtable<>();

    private <Model extends BaseModel> Set<ChangeListener> getListeners(Class<Model> clazz) {
        if (mChangeListeners.containsKey(clazz)) {
            return mChangeListeners.get(clazz);
        }
        return null;
    }

    /**
     * 注册监听器
     *
     * @param clazz    类型
     * @param listener 添加的监听
     * @param <Model>  监听的数据库表
     */
    public static <Model extends BaseModel> void addChangeListener(Class<Model> clazz,
                                                                   ChangeListener<Model> listener) {
        Set<ChangeListener> changeListeners = instance.getListeners(clazz);
        if (changeListeners == null) {
            changeListeners = new HashSet<>();
            mChangeListeners.put(clazz, changeListeners);
        }
        changeListeners.add(listener);
    }

    /**
     * 注销监听器
     *
     * @param clazz    类型
     * @param listener 添加的监听
     * @param <Model>  监听的数据库表
     */
    public static <Model extends BaseModel> void removeChangeListener(Class<Model> clazz,
                                                                      ChangeListener<Model> listener) {
        Set<ChangeListener> listeners = instance.getListeners(clazz);
        if (listeners != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }


    /**
     * 通用的数据库保存操作
     *
     * @param clazz   数据的类型,操作的表名
     * @param models  需要保存的数据数组
     * @param <Model> 数据模型
     */
    @SuppressWarnings("unchecked")
    public static <Model extends BaseModel> void save(final Class<Model> clazz, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        //保存到数据库
        DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
        database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                FlowManager.getModelAdapter(clazz)
                        .saveAll(Arrays.asList(models));
                instance.notifySave(clazz, models);
            }
        }).build().execute();
    }

    /**
     * 通用的数据库删除操作
     *
     * @param clazz   数据的类型,操作的表名
     * @param models  需要保存的数据数组
     * @param <Model> 数据模型
     */
    @SuppressWarnings("unchecked")
    public static <Model extends BaseModel> void delete(final Class<Model> clazz, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        //保存到数据库
        DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
        database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                FlowManager.getModelAdapter(clazz)
                        .deleteAll(Arrays.asList(models));
                instance.notifyDelete(clazz, models);
            }
        }).build().execute();
    }


    /**
     * 通知数据有保存
     *
     * @param clazz   数据的类型,操作的表名
     * @param models  需要保存的数据数组
     * @param <Model> 数据模型
     */
    @SuppressWarnings("unchecked")
    private <Model extends BaseModel> void notifySave(final Class<Model> clazz, final Model... models) {
        Set<ChangeListener> changeListeners = instance.getListeners(clazz);
        if (changeListeners != null && changeListeners.size() > 0) {
            for (ChangeListener listener : changeListeners) {
                //通用的通知
                listener.onDataSave(models);
            }
        }

        //额外的通知
        if (clazz.equals(GroupMember.class)) {
            final GroupMember[] members = (GroupMember[]) models;
            updateGroup(members);
        } else if (clazz.equals(Message.class)) {
            final Message[] messages = (Message[]) models;
            updateSession(messages);
        }

    }

    /**
     * 更新会话列表
     *
     * @param messages 变更的消息
     */
    private void updateSession(Message[] messages) {
        final Set<Session.Identify> identifies = new HashSet<>();
        //获取会话的唯一标识
        for (Message message : messages) {
            Session.Identify identify = Session.createSessionIdentify(message);
            identifies.add(identify);
        }

        //保存到数据库
        DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
        database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session [] sessions = new Session[identifies.size()];
                int index = 0;
                for (Session.Identify identify : identifies) {
                    //从本地查找会话记录
                    Session session = SessionHelper.findFromLocal(identify.id);
                    if (session == null) {
                        //没找到代表第一次通话,需要增加一条会话记录
                        session = new Session(identify);
                    }

                    //更新到最新的状态
                    session.updateToNow();

                    adapter.save(session);

                    sessions[index++] = session;
                }

                //再次通知更新
                notifySave(Session.class,sessions);
            }
        }).build().execute();

    }

    /**
     * 更新群信息
     *
     * @param members 变化的群成员列表
     */
    private void updateGroup(GroupMember[] members) {
        final Set<String> groupIds = new HashSet<>();
        //利用set集合去重
        for (GroupMember member : members) {
            groupIds.add(member.getGroup().getId());
        }

        //保存到数据库
        DatabaseDefinition database = FlowManager.getDatabase(AppDatabase.class);
        database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                List<Group> groups = SQLite.select()
                        .from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();

                //进行二次通知
                notifySave(Group.class, groups.toArray(new Group[0]));
            }
        }).build().execute();

    }

    /**
     * 通知数据有删除
     *
     * @param clazz   数据的类型,操作的表名
     * @param models  需要保存的数据数组
     * @param <Model> 数据模型
     */
    @SuppressWarnings("unchecked")
    private <Model extends BaseModel> void notifyDelete(final Class<Model> clazz, final Model... models) {
        Set<ChangeListener> changeListeners = instance.getListeners(clazz);
        if (changeListeners != null) {
            for (ChangeListener listener : changeListeners) {
                listener.onDataDelete(models);
            }
        }

        if (clazz.equals(GroupMember.class)) {
            updateGroup((GroupMember[]) models);
        } else if (clazz.equals(Message.class)) {
            updateSession((Message[]) models);
        }

    }

    @SuppressWarnings("unchecked")
    public interface ChangeListener<Data extends BaseModel> {

        void onDataSave(Data... list);

        void onDataDelete(Data... list);
    }


}
