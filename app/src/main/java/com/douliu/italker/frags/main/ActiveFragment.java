package com.douliu.italker.frags.main;


import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.douliu.italker.R;
import com.douliu.italker.activities.MessageActivity;
import com.example.commom.app.PresenterFragment;
import com.example.commom.utils.TimeUtils;
import com.example.commom.widget.EmptyView;
import com.example.commom.widget.PortraitView;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.Session;
import com.example.factory.presenter.session.SessionContract;
import com.example.factory.presenter.session.SessionPresenter;

import butterknife.BindView;


public class ActiveFragment extends PresenterFragment<SessionContract.Presenter>
        implements SessionContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<Session> mAdapter;


    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<Session> holder, Session session) {
                MessageActivity.show(getContext(), session);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    protected SessionContract.Presenter createPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {

        @BindView(R.id.txt_name)
        TextView mTxtName;
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;
        @BindView(R.id.txt_content)
        TextView mTxtContent;
        @BindView(R.id.txt_time)
        TextView mTxtTime;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(Session session) {
            mPortraitView.setup(Glide.with(ActiveFragment.this), session.getPicture());
            mTxtName.setText(session.getTitle());
            mTxtContent.setText(TextUtils.isEmpty(session.getContent()) ? "" : session.getContent());
            mTxtTime.setText(TimeUtils.getFriendlyTimeSpanByNow(session.getModifyAt()));
        }
    }

}
