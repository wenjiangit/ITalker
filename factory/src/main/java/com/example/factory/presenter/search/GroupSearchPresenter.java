package com.example.factory.presenter.search;

import com.example.commom.factory.data.DataSource;
import com.example.commom.factory.presenter.BasePresenter;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.net.Network;

import net.qiujuer.genius.kit.handler.Run;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
      /*  if (mCall != null && !mCall.isCanceled()) {//避免上次搜索没有完成,又触发下一次搜索
            mCall.cancel();
        }
        mCall = GroupHelper.search(content, this);*/
        Disposable disposable = Network.remote()
                .rxGroupSearch(content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listRspModel -> {
                    if (listRspModel != null && listRspModel.success()) {
                        List<GroupCard> groupCards = listRspModel.getResult();
                        getView().onSearchDone(groupCards);
                    } else {
                        Factory.decodeRspCode(listRspModel, strRes -> getView().showError(strRes));
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    getView().showError(R.string.data_network_error);
                });
        addDisposable(disposable);
    }

    @Override
    public void onDataLoaded(final List<GroupCard> response) {
        final SearchContract.GroupView view = getView();
        if (view == null) {
            return;
        }

        //强制在主线程更新UI
        Run.onUiAsync(() -> view.onSearchDone(response));
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final SearchContract.GroupView view = getView();
        if (view == null) {
            return;
        }

        //强制在主线程更新UI
        Run.onUiAsync(() -> view.showError(strRes));
    }
}
