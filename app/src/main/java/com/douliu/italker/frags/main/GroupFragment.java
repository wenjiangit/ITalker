package com.douliu.italker.frags.main;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.douliu.italker.R;
import com.douliu.italker.activities.MessageActivity;
import com.example.commom.app.PresenterFragment;
import com.example.commom.widget.EmptyView;
import com.example.commom.widget.PortraitView;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.Group;
import com.example.factory.presenter.group.GroupContract;
import com.example.factory.presenter.group.GroupPresenter;

import butterknife.BindView;


public class GroupFragment extends PresenterFragment<GroupContract.Presenter>
        implements GroupContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmpty;


    private GroupAdapter mAdapter;

    public GroupFragment() {
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new GroupAdapter();
        mRecycler.setAdapter(mAdapter);

        mEmpty.bind(mRecycler);
        setPlaceHolderView(mEmpty);

        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<Group>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<Group> holder, Group group) {
                MessageActivity.show(getContext(), group.getId());
            }
        });

    }

    @Override
    public RecyclerAdapter<Group> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mEmpty.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected GroupContract.Presenter createPresenter() {
        return new GroupPresenter(this);
    }

    class GroupAdapter extends RecyclerAdapter<Group> {

        @Override
        protected int getItemViewType(int position, Group group) {
            return R.layout.cell_group_list;
        }

        @Override
        protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
            return new GroupFragment.ViewHolder(root);
        }

    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<Group> {

        @BindView(R.id.im_portrait)
        PortraitView mImPortrait;
        @BindView(R.id.txt_name)
        TextView mTxtName;
        @BindView(R.id.txt_desc)
        TextView mTxtDesc;
        @BindView(R.id.txt_member)
        TextView mTxtMember;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(Group group) {
            mImPortrait.setup(Glide.with(GroupFragment.this), group.getPicture());
            mTxtName.setText(group.getName());
            mTxtDesc.setText(group.getDesc());
            if (group.holder != null && group.holder instanceof String) {
                mTxtMember.setText((String) group.holder);
            } else {
                mTxtMember.setText("");
            }
        }
    }

}
