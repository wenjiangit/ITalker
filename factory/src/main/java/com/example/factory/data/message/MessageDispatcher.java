package com.example.factory.data.message;


import android.text.TextUtils;

import com.example.factory.data.helper.DbHelper;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.helper.MessageHelper;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 消息统一分发处理
 * <p>
 *
 * @author douliu
 * @date 2017/6/27
 */

public class MessageDispatcher implements MessageCenter {


    /**
     * 维护一个单线程池进行统一的线程调度
     */
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    public static MessageDispatcher getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 静态内部类实现单例
     */
    private static class InstanceHolder {
        private static final MessageDispatcher INSTANCE = new MessageDispatcher();
    }

    private MessageDispatcher() {
    }


    @Override
    public void dispatch(MessageCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        EXECUTOR.execute(new MessageHandler(cards));
    }

    static class MessageHandler implements Runnable {
        private MessageCard[] mCards;

        MessageHandler(MessageCard[] cards) {
            mCards = cards;
        }

        @Override
        public void run() {
            List<Message> messageList = new ArrayList<>();
            for (MessageCard card : mCards) {
                if (card == null || TextUtils.isEmpty(card.getId())
                        || TextUtils.isEmpty(card.getSenderId())
                        || (TextUtils.isEmpty(card.getReceiverId())
                        && TextUtils.isEmpty(card.getGroupId()))) {
                    //过滤掉不合格的消息
                    continue;
                }

                // 发送消息流程：写消息->存储本地->发送网络->网络返回->刷新本地状态
                // 消息来源  1.来自服务器推送的
                //          2.自己造的
                Message message = MessageHelper.findFromLocal(card.getId());
                if (message != null) {//本地已有
                    if (message.getStatus() == Message.STATUS_DONE) {
                        //本地消息已经处于完成状态,则不做处理
                        continue;
                    }

                    // 刷新本地消息时间
                    if (card.getStatus() == Message.STATUS_DONE) {
                        message.setCreateAt(card.getCreateAt());
                    }

                    // 更新可能会变化的部分
                    message.setAttach(card.getAttach());
                    message.setContent(card.getContent());
                    message.setStatus(card.getStatus());

                } else {
                    // 服务器推送的消息card,自己构建一个完整的message存储到本地
                    User sender = UserHelper.search(card.getSenderId());
                    User receiver = null;
                    Group group = null;
                    if (!TextUtils.isEmpty(card.getReceiverId())) {
                        receiver = UserHelper.search(card.getReceiverId());
                    } else if (!TextUtils.isEmpty(card.getGroupId())) {
                        group = GroupHelper.find(card.getGroupId());
                    }

                    if (sender == null || (receiver == null && group == null)) {
                        continue;
                    }
                    message = card.build(sender, receiver, group);
                }
                messageList.add(message);
            }

            if (messageList.size() > 0) {
                DbHelper.save(Message.class, messageList.toArray(new Message[0]));
            }
        }
    }

}
