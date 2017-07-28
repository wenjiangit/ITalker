package com.example.factory.presenter.group;

import com.example.commom.factory.presenter.BaseContract;
import com.example.factory.model.sample.MemberUserModel;

/**
 * 群成员契约
 * Created by douliu on 2017/7/28.
 */

public interface GroupMemberContract {

    interface Presenter extends BaseContract.Presenter{

        void refresh();
    }

    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {

    }


}
