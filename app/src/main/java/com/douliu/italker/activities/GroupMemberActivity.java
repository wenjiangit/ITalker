package com.douliu.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.douliu.italker.R;
import com.example.commom.app.PresenterToolbarActivity;
import com.example.commom.widget.PortraitView;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.model.sample.MemberUserModel;
import com.example.factory.presenter.group.GroupMemberContract;
import com.example.factory.presenter.group.GroupMemberPresenter;

import butterknife.BindView;

public class GroupMemberActivity extends PresenterToolbarActivity<GroupMemberContract.Presenter>
        implements GroupMemberContract.View {


    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<MemberUserModel> mAdapter;
    private static final String EXTRA_GROUP_ID = "extra_group_id";

    private String mGroupId;

    public static void show(Context context, String groupId) {
        Intent intent = new Intent(context, GroupMemberActivity.class)
                .putExtra(EXTRA_GROUP_ID, groupId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_member;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mGroupId = bundle.getString(EXTRA_GROUP_ID);
        return !TextUtils.isEmpty(mGroupId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new Adapter());
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public RecyclerAdapter<MemberUserModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        hideLoading();
    }

    @Override
    protected GroupMemberContract.Presenter createPresenter() {
        return new GroupMemberPresenter(this, mGroupId);
    }

    class Adapter extends RecyclerAdapter<MemberUserModel>{
        @Override
        protected int getItemViewType(int position, MemberUserModel memberUserModel) {
            return R.layout.cell_group_create_contact;
        }

        @Override
        protected ViewHolder<MemberUserModel> onCreateViewHolder(View root, int viewType) {
            return new GroupMemberActivity.ViewHolder(root);
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<MemberUserModel>{

        @BindView(R.id.txt_name)
        TextView mTxtName;
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cb_select).setVisibility(View.GONE);
        }

        @Override
        public void onBind(MemberUserModel model) {
            mTxtName.setText(model.name);
            mPortraitView.setup(Glide.with(GroupMemberActivity.this), model.portrait);
        }
    }


}
