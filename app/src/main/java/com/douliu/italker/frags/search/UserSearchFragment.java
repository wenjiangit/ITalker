package com.douliu.italker.frags.search;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.douliu.italker.R;
import com.douliu.italker.activities.PersonalActivity;
import com.douliu.italker.activities.SearchActivity;
import com.example.commom.app.Application;
import com.example.commom.app.PresenterFragment;
import com.example.commom.widget.EmptyView;
import com.example.commom.widget.PortraitView;
import com.example.commom.widget.recycler.DefaultItemDecoration;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.contact.FollowContract;
import com.example.factory.presenter.contact.FollowPresenter;
import com.example.factory.presenter.search.SearchContract;
import com.example.factory.presenter.search.UserSearchPresenter;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户搜索的fragment
 */
public class UserSearchFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchContract.UserView, SearchActivity.SearchFragment {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<UserCard> mAdapter;

    public UserSearchFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_user_search;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.addItemDecoration(new DefaultItemDecoration(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new UserSearchFragment.ViewHolder(root);
            }
        });
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        mAdapter.replace(userCards);

        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected SearchContract.Presenter createPresenter() {
        return new UserSearchPresenter(this);
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> implements FollowContract.View{

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.im_follow)
        ImageView mImFollow;

        private FollowContract.Presenter mPresenter;

        ViewHolder(View itemView) {
            super(itemView);
            //将View和presenter进行绑定
            new FollowPresenter(this);
        }

        @Override
        public void onBind(UserCard userCard) {
            mPortraitView.setup(Glide.with(UserSearchFragment.this), userCard);
            mTvName.setText(userCard.getName());
            mImFollow.setEnabled(!userCard.isFollow());
        }

        @OnClick(R.id.im_follow)
        void onFollow() {
            mPresenter.follow(mData.getId());
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            PersonalActivity.show(getContext(),mData.getId());
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void showError(int strId) {
            Application.showToast(strId);
            if (mImFollow.getDrawable() instanceof LoadingDrawable) {
                LoadingDrawable drawable = (LoadingDrawable) mImFollow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int min = (int) Ui.dipToPx(getResources(), 22);
            int max = (int) Ui.dipToPx(getResources(), 30);
            LoadingDrawable drawable = new LoadingCircleDrawable(min,max);
            drawable.setForegroundColor(new int[]{ContextCompat.getColor(getContext(),R.color.white)});
            drawable.setBackgroundColor(R.color.colorAccent);
            mImFollow.setImageDrawable(drawable);
            drawable.start();
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            if (mImFollow.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) mImFollow.getDrawable()).stop();
            }
            mImFollow.setImageResource(R.drawable.sel_opt_done_add);
            updateData(userCard);
        }
    }


}
