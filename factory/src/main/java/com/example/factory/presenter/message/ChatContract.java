package com.example.factory.presenter.message;

import android.support.v7.widget.RecyclerView;

import com.example.commom.factory.presenter.BaseContract;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.model.sample.MemberUserModel;

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

        RecyclerView getRecyclerView();
    }


    interface UserView extends View<User>{

    }

    interface GroupView extends View<Group> {

        void showAdminOption(boolean isAdmin);

        void showGroupMemberInfo(List<MemberUserModel> models, int moreCount);
    }



}
