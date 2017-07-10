package com.example.factory.data.group;

import android.text.TextUtils;

import com.example.factory.data.helper.DbHelper;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.GroupMember;
import com.example.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * Created by douliu on 2017/6/27.
 */

public class GroupDispatcher implements GroupCenter {

    private static GroupDispatcher instance;

    //维护一个单线程池进行统一的线程调度
    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static GroupDispatcher instance() {
        if (instance == null) {
            synchronized (GroupDispatcher.class) {
                if (instance == null) {
                    instance = new GroupDispatcher();
                }
            }
        }
        return instance;
    }

    private GroupDispatcher() {
    }


    @Override
    public void dispatch(GroupCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }

        executor.execute(new GroupCardHandler(cards));
    }

    @Override
    public void dispatch(GroupMemberCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }

        executor.execute(new GroupMemberCardHandler(cards));
    }


    static class GroupCardHandler implements Runnable {
        private final GroupCard[] mCards;

        GroupCardHandler(GroupCard[] cards) {
            mCards = cards;
        }

        @Override
        public void run() {
            List<Group> groupList = new ArrayList<>();
            for (GroupCard card : mCards) {
                if (card == null || TextUtils.isEmpty(card.getId())
                        || TextUtils.isEmpty(card.getOwnerId())) {
                    continue;
                }

                User user = UserHelper.search(card.getOwnerId());
                if (user != null) {
                    Group group = card.build(user);
                    groupList.add(group);
                }
            }

            if (groupList.size() > 0) {
                DbHelper.save(Group.class, groupList.toArray(new Group[0]));
            }
        }
    }


    static class GroupMemberCardHandler implements Runnable {
        private final GroupMemberCard[] mCards;

        GroupMemberCardHandler(GroupMemberCard[] cards) {
            mCards = cards;
        }

        @Override
        public void run() {
            List<GroupMember> members = new ArrayList<>();
            for (GroupMemberCard card : mCards) {
                if (card == null || TextUtils.isEmpty(card.getId())
                        || TextUtils.isEmpty(card.getUserId())
                        || TextUtils.isEmpty(card.getGroupId())) {
                    continue;
                }

                //查找群
                Group group = GroupHelper.find(card.getGroupId());
                //查找人
                User user = UserHelper.search(card.getUserId());
                if (group != null && user != null) {
                    GroupMember member = card.build(group, user);
                    members.add(member);
                }

            }

            if (members.size() > 0) {
                DbHelper.save(GroupMember.class, members.toArray(new GroupMember[0]));
            }
        }
    }
}


