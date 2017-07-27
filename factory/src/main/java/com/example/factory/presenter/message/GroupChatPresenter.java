package com.example.factory.presenter.message;

import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.message.MessageGroupRepository;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.sample.MemberUserModel;

import java.util.List;

/**
 * 群聊的逻辑处理类
 * Created by wenjian on 2017/7/8.
 */

public class GroupChatPresenter extends ChatPresenter<Group,ChatContract.GroupView> {

    public GroupChatPresenter(ChatContract.GroupView view, String receiverId) {
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();

        Group group = GroupHelper.findFromLocal(mReceiverId);
        if (group != null) {
            getView().onInit(group);
            List<MemberUserModel> sampleData = group.getLatelySampleData();
            long memberCount = group.getGroupMemberCount();
            if (sampleData == null || sampleData.size() == 0 || memberCount == 0) {
                return;
            }

            getView().showGroupMemberInfo(sampleData, (int) (memberCount > 4 ? memberCount - 4 : memberCount));

//            getView().showAdminOption();

        }


    }
}
