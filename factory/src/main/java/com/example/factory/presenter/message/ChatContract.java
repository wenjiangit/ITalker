package com.example.factory.presenter.message;

import com.example.commom.factory.presenter.BaseContract;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;

import java.util.List;

/**
 * 聊天契约
 * Created by wenjian on 2017/7/8.
 */

public interface ChatContract {

    interface Presenter extends BaseContract.Presenter{
        void pushText(String content);

        void pushAudio(String path);

        void pushPics(List<String> pathList);

        boolean rePush(Message message);

    }

    interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message> {

        void onInit(InitModel model);
    }


    interface UserView extends View<User>{

    }

    interface GroupView extends View<Group> {
    }



}
