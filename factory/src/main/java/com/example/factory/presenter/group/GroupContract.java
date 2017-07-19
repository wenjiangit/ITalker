package com.example.factory.presenter.group;

import com.example.commom.factory.presenter.BaseContract;
import com.example.factory.model.db.Group;

/**
 *
 * Created by douliu on 2017/7/19.
 */

public interface GroupContract {

    interface Presenter extends BaseContract.Presenter {
    }


    interface View extends BaseContract.RecyclerView<Presenter, Group> {
    }



}
