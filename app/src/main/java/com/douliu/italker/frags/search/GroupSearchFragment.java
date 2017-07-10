package com.douliu.italker.frags.search;


import com.douliu.italker.R;
import com.douliu.italker.activities.SearchActivity;
import com.example.commom.app.PresenterFragment;
import com.example.factory.model.card.GroupCard;
import com.example.factory.presenter.search.SearchContract;

import java.util.List;

/**
 * 群搜索界面
 */
public class GroupSearchFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchContract.GroupView,SearchActivity.SearchFragment {


    public GroupSearchFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group_search;
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {

    }

    @Override
    protected SearchContract.Presenter createPresenter() {
        return null;
    }

    @Override
    public void search(String content) {

    }
}
