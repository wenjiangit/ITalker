package com.example.factory.presenter.search;

import android.support.v7.util.DiffUtil;

import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.commom.factory.presenter.BaseSourcePresenter;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.data.user.ContactDataSource;
import com.example.factory.data.user.ContactRepository;
import com.example.factory.model.db.User;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

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
        UserHelper.refreshContacts();
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
