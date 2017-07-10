package com.douliu.italker.frags.mian;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.douliu.italker.R;
import com.douliu.italker.activities.MessageActivity;
import com.douliu.italker.activities.PersonalActivity;
import com.example.commom.app.PresenterFragment;
import com.example.commom.widget.EmptyView;
import com.example.commom.widget.PortraitView;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.User;
import com.example.factory.presenter.search.ContactContract;
import com.example.factory.presenter.search.ContactPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactFragment extends PresenterFragment<ContactContract.Presenter>
        implements ContactContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<User> mAdapter;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<User>() {
            @Override
            protected int getItemViewType(int position, User userCard) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);

        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<User> holder, User user) {
                MessageActivity.show(getContext(), user);
            }
        });

    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected ContactContract.Presenter createPresenter() {
        return new ContactPresenter(this);
    }

    //不能private,否则butterknife编译时不能访问到
    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.txt_desc)
        TextView mTxtDesc;
        @BindView(R.id.txt_name)
        TextView mTxtName;
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(User user) {
            mPortraitView.setup(Glide.with(ContactFragment.this), user);
            mTxtDesc.setText(user.getDesc());
            mTxtName.setText(user.getName());
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            PersonalActivity.show(getContext(),mData.getId());
        }

    }

}
