package com.example.factory.presenter.search;

import com.example.commom.factory.presenter.BasePresenter;

/**
 * Created by douliu on 2017/6/22.
 */

public class GroupSearchPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter {
    public GroupSearchPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
