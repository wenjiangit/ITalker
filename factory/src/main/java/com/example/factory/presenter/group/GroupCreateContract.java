package com.example.factory.presenter.group;

import com.example.commom.factory.model.Author;
import com.example.commom.factory.presenter.BaseContract;

/**
 *
 * Created by douliu on 2017/7/18.
 */

public interface GroupCreateContract {

    interface Presenter extends BaseContract.Presenter{

        void create(String name, String desc, String picture);

        void changeSelected(ViewModel model,boolean isSelected);

    }

    interface View extends BaseContract.RecyclerView<Presenter,ViewModel>{

        void onCreateSucceed();

    }

    class ViewModel{
        public Author author;
        public boolean isSelected;
    }


}
