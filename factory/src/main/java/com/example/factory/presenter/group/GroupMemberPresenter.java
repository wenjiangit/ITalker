package com.example.factory.presenter.group;

import com.example.commom.factory.presenter.BaseRecyclerPresenter;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.db.GroupMember;
import com.example.factory.model.db.GroupMember_Table;
import com.example.factory.model.db.User;
import com.example.factory.model.sample.MemberUserModel;
import com.example.factory.net.Network;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Created by douliu on 2017/7/28.
 */

public class GroupMemberPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMemberContract.View>
        implements GroupMemberContract.Presenter {

    private String mGroupId;

    public GroupMemberPresenter(GroupMemberContract.View view,String groupId) {
        super(view);
        mGroupId = groupId;
    }

    @Override
    public void start() {
        super.start();

        List<GroupMember> members = SQLite.select()
                .from(GroupMember.class)
                .where(GroupMember_Table.group_id.eq(mGroupId))
                .queryList();


        List<MemberUserModel> modelList = new ArrayList<>();
        for (GroupMember member : members) {
            User user = member.getUser();
            user.load();
            MemberUserModel model = new MemberUserModel(user);
            modelList.add(model);
        }

        refreshData(modelList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refresh() {
        Network.remote().members(mGroupId)
                .subscribeOn(Schedulers.io())
                .flatMap(rspModel -> {
                    List<GroupMemberCard> memberCards = rspModel.getResult();
                        List<User> users = new ArrayList<>();
                        for (GroupMemberCard memberCard : memberCards) {
                            String userId = memberCard.getUserId();
                            User user = UserHelper.search(userId);
                            users.add(user);
                        }
                        return Observable.fromArray(users);

                })
                .map(users -> {
                    List<MemberUserModel> models = new ArrayList<>();
                    for (User user : users) {
                        MemberUserModel model = new MemberUserModel(user);
                        models.add(model);
                    }
                    return models;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::refreshData);
    }
}
