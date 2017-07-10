package com.douliu.italker.frags.message;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.douliu.italker.R;
import com.douliu.italker.activities.MessageActivity;
import com.example.commom.app.BaseFragment;
import com.example.commom.app.PresenterFragment;
import com.example.commom.factory.presenter.BaseContract;
import com.example.commom.widget.PortraitView;
import com.example.commom.widget.TextWatcherAdapter;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.persistant.Account;
import com.example.factory.presenter.message.ChatContract;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 * Created by douliu on 2017/7/6.
 */

public abstract class ChatFragment<InitModel> extends PresenterFragment<ChatContract.Presenter>
        implements AppBarLayout.OnOffsetChangedListener,ChatContract.View<InitModel> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.edit_content)
    EditText mEditContent;
    @BindView(R.id.btn_submit)
    ImageView mBtnSubmit;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;


    protected String mReceiverId;
    private ChatAdapter mAdapter;

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        mReceiverId = arguments.getString(MessageActivity.EXTRA_RECEIVER_ID);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        initToolbar();
        initAppbar();
        initEditText();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initEditText() {
        mEditContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                boolean needSend = !TextUtils.isEmpty(s);
                mBtnSubmit.setActivated(needSend);
            }
        });
    }


    private void initAppbar() {
        mAppbar.addOnOffsetChangedListener(this);
    }


    protected void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }

    @OnClick(R.id.btn_face)
    void onFaceClick() {

    }

    @OnClick(R.id.btn_record)
    void onRecordClick() {

    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mBtnSubmit.isActivated()) {
            sendMsg();
        } else {
            onMoreClick();
        }

    }

    protected void sendMsg() {
        String content = mEditContent.getText().toString();
        mEditContent.setText("");
        mPresenter.pushText(content);
    }

    protected void onMoreClick() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        //do nothing 没有占位布局
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }


    private class ChatAdapter extends RecyclerAdapter<Message> {
        @Override
        protected int getItemViewType(int position, Message message) {
            boolean isRight = message.getSender().getId().equalsIgnoreCase(Account.getUserId());
            switch (message.getType()) {
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;
                case Message.TYPE_FILE:
                    return isRight ? R.layout.cell_chat_file_right : R.layout.cell_chat_file_left;
            }
            return 0;
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                case R.layout.cell_chat_text_right:
                case R.layout.cell_chat_text_left:
                    return new TextViewHolder(root);

                case R.layout.cell_chat_pic_left:
                case R.layout.cell_chat_pic_right:
                    return new PicViewHolder(root);

                case R.layout.cell_chat_audio_left:
                case R.layout.cell_chat_audio_right:
                    return new AudioViewHolder(root);

                case R.layout.cell_chat_file_left:
                case R.layout.cell_chat_file_right:
                    return new FileViewHolder(root);

                default:
                    return new TextViewHolder(root);
            }
        }
    }


    //基类Holder
    class BaseViewHolder extends RecyclerAdapter.ViewHolder<Message> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(Message message) {
            User sender = message.getSender();
            //load获取懒加载的数据
            sender.load();
            mPortraitView.setup(Glide.with(ChatFragment.this), sender);

            if (mLoading != null) {//右边
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {//发送成功
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {//发送中
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setForegroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    mLoading.setProgress(0);
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {//发送失败
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setForegroundColor(ContextCompat.getColor(getContext(), R.color.alertImportant));
                    mLoading.setProgress(1);
                    mLoading.stop();
                }

                //发送失败允许重新发送
                mPortraitView.setEnabled(status == Message.STATUS_FAILED);
            }
        }

        @OnClick(R.id.im_portrait)
        void onReSendMessage() {
            if (mLoading != null && mPresenter.rePush(mData)) {
                updateData(mData);
            }
        }
    }

    //文本
    class TextViewHolder extends BaseViewHolder {

        @BindView(R.id.txt_content)
        TextView mTxtContent;

        public TextViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(Message message) {
            super.onBind(message);
            mTxtContent.setText(message.getContent());
        }
    }

    //图片
    class PicViewHolder extends BaseViewHolder {

        public PicViewHolder(View itemView) {
            super(itemView);
        }
    }

    //语音
    class AudioViewHolder extends BaseViewHolder {

        public AudioViewHolder(View itemView) {
            super(itemView);
        }
    }

    //文件
    class FileViewHolder extends BaseViewHolder {

        public FileViewHolder(View itemView) {
            super(itemView);
        }
    }


}
