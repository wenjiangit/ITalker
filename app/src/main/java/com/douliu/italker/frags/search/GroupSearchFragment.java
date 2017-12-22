package com.douliu.italker.frags.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.douliu.italker.R;
import com.douliu.italker.activities.PersonalActivity;
import com.douliu.italker.activities.SearchActivity;
import com.example.commom.app.PresenterFragment;
import com.example.commom.widget.EmptyView;
import com.example.commom.widget.PortraitView;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.model.card.GroupCard;
import com.example.factory.presenter.search.GroupSearchPresenter;
import com.example.factory.presenter.search.SearchContract;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群搜索界面
 */
public class GroupSearchFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchContract.GroupView, SearchActivity.SearchFragment {


    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<GroupCard> mAdapter;

    public GroupSearchFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group_search;
    }

    @Override
    protected void initWidget(final View rootView) {
        super.initWidget(rootView);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<GroupCard>() {
            @Override
            protected int getItemViewType(int position, GroupCard groupCard) {
                return R.layout.cell_group_search_list;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View root, int viewType) {
                return new GroupSearchFragment.ViewHolder(root);
            }
        });
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.search("");
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {
        mAdapter.replace(groupCards);

        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected SearchContract.Presenter createPresenter() {
        return new GroupSearchPresenter(this);
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.im_join)
        ImageView mImJoin;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(GroupCard groupCard) {
            mPortraitView.setup(Glide.with(GroupSearchFragment.this), groupCard.getPicture());
            mTvName.setText(groupCard.getName());
            mImJoin.setEnabled(groupCard.getJoinAt() == null);
        }

        @OnClick(R.id.im_join)
        void onJoinClick() {
            PersonalActivity.show(getActivity(), mData.getOwnerId());
        }

    }

}
