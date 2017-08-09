package com.example.factory.presenter.group;

import com.example.commom.factory.presenter.BaseRecyclerPresenter;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.db.User;
import com.example.factory.model.sample.MemberUserModel;
import com.example.factory.net.Network;
import com.example.factory.rx.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by douliu on 2017/7/28.
 */

public class GroupMemberPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMemberContract.View>
        implements GroupMemberContract.Presenter {

    private String mGroupId;

    public GroupMemberPresenter(GroupMemberContract.View view, String groupId) {
        super(view);
        mGroupId = groupId;
    }

    @Override
    public void start() {
        super.start();
        //-1 查询所有
        Disposable disposable = Observable.just(GroupHelper.getLatelyGroupMembers(mGroupId, -1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MemberUserModel>>() {
                    @Override
                    public void accept(List<MemberUserModel> models) throws Exception {
                        refreshDataByRx(models);
                    }
                });
        addDisposable(disposable);
    }

    @Override
    public void refresh() {
        Network.remote().members(mGroupId)
                .subscribeOn(Schedulers.io())
                .map(RxUtils.<List<GroupMemberCard>>convert())
                .map(new Function<List<GroupMemberCard>, List<MemberUserModel>>() {
                    @Override
                    public List<MemberUserModel> apply(List<GroupMemberCard> groupMemberCards) throws Exception {
                        List<MemberUserModel> models = new ArrayList<>();
                        for (GroupMemberCard memberCard : groupMemberCards) {
                            String userId = memberCard.getUserId();
                            User user = UserHelper.search(userId);
                            MemberUserModel model = new MemberUserModel(user);
                            models.add(model);
                        }
                        return models;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        addDisposable(disposable);
                    }
                })
                .subscribe(new Consumer<List<MemberUserModel>>() {
                    @Override
                    public void accept(List<MemberUserModel> models) throws Exception {
                        refreshDataByRx(models);
                    }
                });

    }
}
