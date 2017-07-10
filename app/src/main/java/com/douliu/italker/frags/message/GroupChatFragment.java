package com.douliu.italker.frags.message;


import android.support.v4.app.Fragment;

import com.douliu.italker.R;
import com.example.factory.model.db.Group;
import com.example.factory.presenter.message.ChatContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends ChatFragment<Group>
        implements ChatContract.GroupView {


    public GroupChatFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group_chat;
    }

    @Override
    protected ChatContract.Presenter createPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }
}
