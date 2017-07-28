package com.example.factory.data.message;

import android.support.annotation.NonNull;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * 群消息仓库
 * Created by wenjian on 2017/7/8.
 */

public class MessageGroupRepository extends BaseDbRepository<Message>
        implements MessageDataSource {

    //消息接收群id
    private String mReceiverId;
    public MessageGroupRepository(String receiverId) {
        super();
        this.mReceiverId = receiverId;
    }


    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);
        // 加载本地群消息
        SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(mReceiverId))
                .limit(30)
                .orderBy(Message_Table.createAt,false)//拉取最新的30条数据,所以要时间逆序
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        return message.getGroup() != null && message.getGroup().getId().equals(mReceiverId);
    }


    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        //然后再将获取到的数据反向
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);

    }
}
