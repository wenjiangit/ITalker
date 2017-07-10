package com.example.factory.presenter.contact;

import com.example.commom.factory.data.DataSource;
import com.example.commom.factory.presenter.BasePresenter;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 *
 * Created by wenjian on 2017/6/23.
 */

public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter, DataSource.Callback<UserCard> {

    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String userId) {
        start();
        UserHelper.follow(userId, this);
    }

    @Override
    public void onDataLoaded(final UserCard response) {
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSucceed(response);
                }
            });
        }

    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final FollowContract.View view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
