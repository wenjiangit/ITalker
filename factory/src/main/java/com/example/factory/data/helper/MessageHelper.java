package com.example.factory.data.helper;

import com.example.factory.Factory;
import com.example.factory.model.api.MessageCreateModel;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Message_Table;
import com.example.factory.net.Network;
import com.example.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by douliu on 2017/6/28.
 */

public class MessageHelper {

    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    public static void push(final MessageCreateModel model) {
        Factory.runOnBackground(new Runnable() {
            @Override
            public void run() {
                Message message = MessageHelper.findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED) {
                    //本地有且不是失败状态，则不走发送流程
                    return;
                }

                //新建一条消息
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);

                //调用服务器接口进行发送
                RemoteService service = Network.remote();
                Call<RspModel<MessageCard>> call = service.msgPush(model);
                call.enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            Factory.getMessageCenter().dispatch(rspCard);
                        } else {
                            //解析错误信息
                            Factory.decodeRspCode(rspModel, null);
                            //更新消息状态为失败
                            onFailure(null, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        //更新消息状态为失败
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }

    public static Message findLastWithGroup(String groupId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.receiver_id.eq(groupId))
                .orderBy(Message_Table.createAt,false)
                .querySingle();
    }

    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.group_id.isNull())
                        .and(Message_Table.sender_id.eq(userId)))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt,false)
                .querySingle();
    }

}
