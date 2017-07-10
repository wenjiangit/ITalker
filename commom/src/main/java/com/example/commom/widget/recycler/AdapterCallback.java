package com.example.commom.widget.recycler;

/**
 *
 * Created by wenjian on 2017/6/2.
 */

public interface AdapterCallback<Data> {

    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
