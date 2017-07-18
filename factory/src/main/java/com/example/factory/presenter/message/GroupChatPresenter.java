package com.example.factory.presenter.message;

import com.example.factory.data.message.MessageRepository;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;

/**
 *
 * Created by wenjian on 2017/7/8.
 */

public class GroupChatPresenter extends ChatPresenter<Group> {

    public GroupChatPresenter(ChatContract.View<Group> view, String receiverId) {
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }
}
