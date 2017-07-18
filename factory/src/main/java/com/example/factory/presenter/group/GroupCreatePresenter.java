package com.example.factory.presenter.group;

import android.text.TextUtils;

import com.example.commom.app.Application;
import com.example.commom.factory.presenter.BaseRecyclerPresenter;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.api.GroupCreateModel;
import com.example.factory.model.db.User;
import com.example.factory.net.UploadHelper;
import com.example.factory.presenter.group.GroupCreateContract.Presenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by douliu on 2017/7/18.
 */

public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel,
        GroupCreateContract.View> implements Presenter {

    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    private final Set<String> mMemberIds = new HashSet<>();

    @Override
    public void start() {
        super.start();
        Factory.runOnBackground(loader);
    }

    @Override
    public void create(final String name, final String desc, final String picture) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)
                || mMemberIds.size() == 0
                || TextUtils.isEmpty(picture)) {
            Application.showToast(R.string.label_group_create_invalid);
        } else {
            Factory.runOnBackground(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.uploadPortrait(picture);
                    if (!TextUtils.isEmpty(url)) {
                        GroupCreateModel model = new GroupCreateModel();
                        model.setDesc(desc);
                        model.setName(name);
                        model.setPicture(url);
                        model.setMemberIds(mMemberIds);
//                        GroupHelper.create(model,this);
                    }
                }
            });

        }

    }

    @Override
    public void changeSelected(GroupCreateContract.ViewModel model, boolean isSelected) {
        if (isSelected) {
            mMemberIds.add(model.author.getId());
        } else {
            mMemberIds.remove(model.author.getId());
        }
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            List<User> contacts = UserHelper.contacts();
            if (contacts.size() == 0) {
                return;
            }

            final List<GroupCreateContract.ViewModel> modelList = new ArrayList<>();
            for (User contact : contacts) {
                GroupCreateContract.ViewModel model = new GroupCreateContract.ViewModel();
                model.author = contact;
                model.isSelected = false;
                modelList.add(model);
            }

            refreshData(modelList);
        }
    };


}
