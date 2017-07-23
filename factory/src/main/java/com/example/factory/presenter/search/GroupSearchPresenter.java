package com.example.factory.presenter.search;

import com.example.commom.factory.data.DataSource;
import com.example.commom.factory.presenter.BasePresenter;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.GroupCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/**
 *
 * Created by douliu on 2017/6/22.
 */

public class GroupSearchPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter, DataSource.Callback<List<GroupCard>> {

    private Call<RspModel<List<GroupCard>>> mCall;

    public GroupSearchPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();
        if (mCall != null && !mCall.isCanceled()) {//避免上次搜索没有完成,又触发下一次搜索
            mCall.cancel();
        }
        mCall = GroupHelper.search(content, this);
    }

    @Override
    public void onDataLoaded(final List<GroupCard> response) {
        final SearchContract.GroupView view = getView();
        if (view == null) {
            return;
        }

        //强制在主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.onSearchDone(response);
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final SearchContract.GroupView view = getView();
        if (view == null) {
            return;
        }

        //强制在主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
