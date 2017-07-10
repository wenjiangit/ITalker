package com.example.factory.data.message;

import android.transition.Slide;

import com.example.commom.factory.data.DataSource;
import com.example.factory.data.BaseDbRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

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
                .orderBy(Message_Table.createAt,false)
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
}
