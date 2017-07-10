package com.example.commom.factory.data;

import android.support.annotation.StringRes;

/**
 * 数据源接口定义
 *
 * Created by douliu on 2017/6/14.
 */

public interface DataSource {


    /**
     * 数据加载回调
     * @param <T> 任意类型
     */
    interface Callback<T> extends SucceedCallback<T>,FailedCallback{

    }



    /**
     * 数据加载成功的回调
     * @param <T> 任意类型
     */
    interface SucceedCallback<T> {
        void onDataLoaded(T response);
    }


    /**
     * 数据加载失败的回调
     */
    interface FailedCallback{
        void onDataNotAvailable(@StringRes int strRes);
    }

    void dispose();

}
