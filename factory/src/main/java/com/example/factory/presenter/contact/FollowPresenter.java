package com.example.factory.presenter.contact;

import com.example.commom.factory.data.DataSource;
import com.example.commom.factory.presenter.BasePresenter;
import com.example.factory.Factory;
import com.example.factory.model.card.UserCard;
import com.example.factory.net.Network;
import com.example.factory.rx.RxResolver;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
//        UserHelper.follow(userId, this);
        Network.rxRemote().userFollow(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        addDisposable(disposable);
                    }
                })
                .subscribe(new RxResolver<>(new DataSource.Callback<UserCard>() {
                    @Override
                    public void onDataNotAvailable(int strRes) {
                        getView().showError(strRes);
                    }

                    @Override
                    public void onDataLoaded(UserCard userCard) {
                        //将数据分发给UserCenter进行处理
                        Factory.getUserCenter().dispatch(userCard);
                        getView().onFollowSucceed(userCard);
                    }
                }));
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
