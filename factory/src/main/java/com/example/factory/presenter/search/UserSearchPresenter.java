package com.example.factory.presenter.search;

import com.example.commom.factory.data.DataSource;
import com.example.commom.factory.presenter.BasePresenter;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 *
 * Created by douliu on 2017/6/22.
 */

public class UserSearchPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter, DataSource.Callback<List<UserCard>> {

    private Call<RspModel<List<UserCard>>> mCall;

    public UserSearchPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();
        if (mCall != null && !mCall.isCanceled()) {//避免上次搜索没有完成,又触发下一次搜索
            mCall.cancel();
        }
        mCall = UserHelper.search(content, this);
    }

    @Override
    public void onDataLoaded(final List<UserCard> userCards) {
        final SearchContract.UserView userView = getView();
        if (userView == null) {
            return;
        }

        //强制在主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                userView.onSearchDone(userCards);
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final SearchContract.UserView userView = getView();
        if (userView == null) {
            return;
        }

        //强制在主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                userView.showError(strRes);
            }
        });
    }
}
