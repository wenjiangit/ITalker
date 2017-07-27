package com.example.factory.presenter.message;

import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.message.MessageRepository;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

import java.util.List;

/**
 *
 * Created by wenjian on 2017/7/8.
 */

public class UserChatPresenter extends ChatPresenter<User,ChatContract.UserView> {

    public UserChatPresenter(ChatContract.UserView view, String receiverId) {
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }


    @Override
    public void start() {
        super.start();
        User user = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(user);
    }

}
