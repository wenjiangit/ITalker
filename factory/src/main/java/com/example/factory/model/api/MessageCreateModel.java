package com.example.factory.model.api;

import com.example.factory.model.card.MessageCard;
import com.example.factory.model.db.Message;
import com.example.factory.persistant.Account;

import java.util.Date;
import java.util.UUID;

/**
 * @author douliu
 * @date 2017/7/4
 */
public class MessageCreateModel {

    private String id;
    //消息内容
    private String content;
    //附件
    private String attach;
    //消息类型,默认为文本消息
    private int type = Message.TYPE_STR;

    private String receiverId;

    //接收类型人或群
    private int receiverType = Message.RECEIVER_TYPE_NONE;

    private MessageCreateModel() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAttach() {
        return attach;
    }

    public int getType() {
        return type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }

    private MessageCard card;

    public MessageCard buildCard() {
        if (card == null) {
            card = new MessageCard();
            card.setId(id);
            card.setAttach(attach);
            card.setContent(content);
            card.setSenderId(Account.getUserId());
            card.setType(type);
            if (receiverType == Message.RECEIVER_TYPE_GROUP) {
                card.setGroupId(receiverId);
            } else {
                card.setReceiverId(receiverId);
            }
            card.setStatus(Message.STATUS_CREATED);
            card.setCreateAt(new Date());
        }
        return card;
    }


    public static MessageCreateModel buildWithMessage(Message message) {
        MessageCreateModel model = new MessageCreateModel();
        model.id = message.getId();
        model.attach = message.getAttach();
        model.content = message.getContent();
        if (message.getReceiver() != null) {
            model.receiverId = message.getReceiver().getId();
            model.receiverType = Message.RECEIVER_TYPE_NONE;
        } else {
            model.receiverId = message.getGroup().getId();
            model.receiverType = Message.RECEIVER_TYPE_GROUP;
        }
        return model;
    }


    public static class Builder {

        private MessageCreateModel mModel;

        public Builder() {
            this.mModel = new MessageCreateModel();
        }

        public Builder receiver(String receiverId, int receiverType) {
            this.mModel.receiverId = receiverId;
            this.mModel.receiverType = receiverType;
            return this;
        }

        public Builder attach(String attach) {
            this.mModel.attach = attach;
            return this;
        }

        public Builder content(String content, int type) {
            this.mModel.content = content;
            this.mModel.type = type;
            return this;
        }

        public MessageCreateModel build() {
            return mModel;
        }
    }


}
