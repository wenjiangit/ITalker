package com.example.factory.presenter.message;

import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.message.MessageGroupRepository;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.sample.MemberUserModel;
import com.example.factory.persistant.Account;

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
            final ChatContract.GroupView view = getView();

            //判断是不是管理人,这里当前只支持群主添加成员 TODO  管理员也可以添加成员
            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
            view.showAdminOption(isAdmin);

            view.onInit(group);

            List<MemberUserModel> members = group.getLatelyGroupMembers();
            long memberCount = group.getGroupMemberCount();

            long size = members.size();
            long moreCount = memberCount > size ? memberCount - size : 0;
            view.showGroupMemberInfo(members, moreCount);
        }


    }
}
