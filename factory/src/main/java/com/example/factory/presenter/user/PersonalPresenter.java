package com.example.factory.presenter.user;

import com.example.commom.factory.presenter.BasePresenter;
import com.example.factory.Factory;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.db.User;
import com.example.factory.persistant.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by wenjian on 2017/6/24.
 */

public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter {

    private User mUser;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        Factory.runOnBackground(new Runnable() {
            @Override
            public void run() {
                final PersonalContract.View view = getView();
                User user = UserHelper.searchFirstNet(view.getUserId());
                onDataLoad(user);
            }
        });
    }

    private void onDataLoad(final User user) {
        this.mUser = user;
        boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        final boolean allow = !isSelf && user.isFollow();
        final boolean follow = user.isFollow();
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                PersonalContract.View view = getView();
                if (view == null) return;
                view.onLoadDone(user);
                view.allowSayHello(allow);
                view.setFollowState(follow);
            }
        });
    }

    @Override
    public User getPersonal() {
        return mUser;
    }
}
