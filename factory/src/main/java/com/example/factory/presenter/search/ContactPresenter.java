package com.example.factory.presenter.search;

import android.support.v7.util.DiffUtil;

import com.example.commom.factory.data.DataSource;
import com.example.commom.factory.presenter.BaseSourcePresenter;
import com.example.commom.utils.CollectionUtil;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.Factory;
import com.example.factory.data.user.ContactDataSource;
import com.example.factory.data.user.ContactRepository;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.net.Network;
import com.example.factory.rx.RxResolver;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Created by wenjian on 2017/6/24.
 */

public class ContactPresenter extends BaseSourcePresenter<User,User,ContactDataSource,ContactContract.View>
        implements ContactContract.Presenter{

    public ContactPresenter(ContactContract.View view) {
        super(new ContactRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        //网络请求
//        UserHelper.refreshContacts();
        Network.rxRemote().userContact()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        addDisposable(disposable);
                    }
                })
                .subscribe(new RxResolver<>(new DataSource.Callback<List<UserCard>>() {
                    @Override
                    public void onDataNotAvailable(int strRes) {
                        getView().showError(strRes);
                    }

                    @Override
                    public void onDataLoaded(List<UserCard> response) {
                        Factory.getUserCenter().dispatch(CollectionUtil.toArray(response,UserCard.class));
                    }
                }));

    }

    @Override
    public void onDataLoaded(List<User> response) {
        ContactContract.View view = getView();
        if (view == null) {
            return;
        }

        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> items = adapter.getItems();

        DiffUiDataCallback<User> callback = new DiffUiDataCallback<>(items, response);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

        refreshData(diffResult,response);
    }

}
