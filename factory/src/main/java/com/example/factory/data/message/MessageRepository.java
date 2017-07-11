package com.example.factory.data.message;

import android.support.annotation.NonNull;

import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 *
 * Created by wenjian on 2017/7/8.
 */

public class MessageRepository extends BaseDbRepository<Message>
        implements MessageDataSource {

    private String mReceiverId;
    public MessageRepository(String receiverId) {
        super();
        this.mReceiverId = receiverId;
    }


    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);
        //对方是发送者且不是群,或者是接收者
        SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.group_id.isNull())
                        .and(Message_Table.sender_id.eq(mReceiverId)))
                .or(Message_Table.receiver_id.eq(mReceiverId))
                .limit(30)
                .orderBy(Message_Table.createAt,false)//拉取最新的30条数据,所以要时间逆序
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        return (mReceiverId.equalsIgnoreCase(message.getSender().getId())
                && message.getGroup() == null)
                || (message.getReceiver() != null
                && mReceiverId.equalsIgnoreCase(message.getReceiver().getId()));
    }


    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        //然后再将获取到的数据反向
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);

    }
}
