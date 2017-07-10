package com.example.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 *
 * Created by wenjian on 2017/6/24.
 */

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {

    private List<T> mOldList,mNewList;

    public DiffUiDataCallback(List<T> oldList, List<T> newList) {
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T oldBean = mOldList.get(oldItemPosition);
        T newBean = mNewList.get(newItemPosition);
        return newBean.isSame(oldBean);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldBean = mOldList.get(oldItemPosition);
        T newBean = mNewList.get(newItemPosition);
        return newBean.isUiContentSame(oldBean);
    }


    public interface UiDataDiffer<T>{

        boolean isSame(T t);

        boolean isUiContentSame(T t);
    }
}
