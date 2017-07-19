package com.example.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import com.example.commom.factory.presenter.BaseSourcePresenter;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.data.group.GroupDataSource;
import com.example.factory.data.group.GroupRepository;
import com.example.factory.data.helper.GroupHelper;
import com.example.factory.model.db.Group;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 *
 * Created by douliu on 2017/7/19.
 */

public class GroupPresenter extends BaseSourcePresenter<Group, Group, GroupDataSource, GroupContract.View>
        implements GroupContract.Presenter {
    public GroupPresenter(GroupContract.View view) {
        super(new GroupRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        //网络请求刷新群列表
        GroupHelper.list("");
    }

    @Override
    public void onDataLoaded(List<Group> response) {
        super.onDataLoaded(response);
        GroupContract.View view = getView();
        if (view == null) return;
        //差异更新
        RecyclerAdapter<Group> adapter = view.getRecyclerAdapter();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(adapter.getItems(), response);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        refreshData(diffResult,response);
    }
}
