package com.douliu.italker.frags.message;


import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.douliu.italker.R;
import com.example.factory.model.db.Group;
import com.example.factory.model.sample.MemberUserModel;
import com.example.factory.presenter.message.ChatContract;
import com.example.factory.presenter.message.GroupChatPresenter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends ChatFragment<Group>
        implements ChatContract.GroupView {

    public GroupChatFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public int getLayoutHeaderId() {
        return R.layout.lay_chat_group_header;
    }

    @Override
    protected ChatContract.Presenter createPresenter() {
        return new GroupChatPresenter(this, mReceiverId);
    }

    @Override
    public void onInit(Group group) {



    }

    @Override
    public void showAdminOption(boolean isAdmin) {
        if (!isAdmin) {
            return;
        }
        mToolbar.inflateMenu(R.menu.group_chat);
        MenuItem menuItem = mToolbar.getMenu().findItem(R.id.action_member_add);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public void showGroupMemberInfo(List<MemberUserModel> models, int moreCount) {

    }
}
