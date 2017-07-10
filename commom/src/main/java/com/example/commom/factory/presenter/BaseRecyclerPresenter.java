package com.example.commom.factory.presenter;

import android.support.v7.util.DiffUtil;

import com.example.commom.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/**
 *
 * Created by douliu on 2017/6/29.
 */

public class BaseRecyclerPresenter<Model,View extends BaseContract.RecyclerView> extends BasePresenter<View>{
    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    /**
     * 刷新数据
     * @param dataList 新的数据
     */
    @SuppressWarnings("unchecked")
    protected void refreshData(final List<Model> dataList) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                final View view = getView();
                if (view == null) return;
                RecyclerAdapter<Model> adapter = view.getRecyclerAdapter();
                adapter.replace(dataList);
                view.onAdapterDataChanged();
            }
        });
    }

    /**
     * 增量刷新数据
     * @param diffResult 对比操作的结果
     * @param dataList 新的数据集
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult, final List<Model> dataList) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                refreshDataOnUiThread(diffResult, dataList);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void refreshDataOnUiThread(DiffUtil.DiffResult diffResult, List<Model> dataList) {
        View view = getView();
        if (view == null) return;
        RecyclerAdapter<Model> adapter = view.getRecyclerAdapter();

        //清除原始数据
        adapter.getItems().clear();
        //添加新数据
        adapter.getItems().addAll(dataList);
        //刷新占位布局
        view.onAdapterDataChanged();
        //通知增量更新
        diffResult.dispatchUpdatesTo(adapter);
    }
}
