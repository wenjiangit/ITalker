package com.douliu.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.douliu.italker.R;
import com.douliu.italker.frags.message.GroupChatFragment;
import com.douliu.italker.frags.message.UserChatFragment;
import com.example.commom.app.BaseActivity;
import com.example.commom.factory.model.Author;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Session;

public class MessageActivity extends BaseActivity {

    private static final String EXTRA_AUTHOR = "extra_author";
    public static final String EXTRA_RECEIVER_ID = "extra_receiver_id";
    private static final String EXTRA_IS_GROUP = "extra_is_group";

    private String mReceiverId;
    private boolean mIsGroup;

    public static void show(Context context, Author author) {
        if (author == null || TextUtils.isEmpty(author.getId())) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(EXTRA_RECEIVER_ID, author.getId());
        intent.putExtra(EXTRA_IS_GROUP, false);
        context.startActivity(intent);
    }


    public static void show(Context context, Session session) {
        if (session == null || TextUtils.isEmpty(session.getId())) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(EXTRA_RECEIVER_ID, session.getId());
        intent.putExtra(EXTRA_IS_GROUP, session.getReceiverType() == Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }


    public static void show(Context context, String receiverId) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(EXTRA_RECEIVER_ID, receiverId);
        intent.putExtra(EXTRA_IS_GROUP, true);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(EXTRA_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(EXTRA_IS_GROUP, false);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Fragment fragment;
        if (mIsGroup) {
            fragment = new GroupChatFragment();
        } else {
            fragment = new UserChatFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_RECEIVER_ID, mReceiverId);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, fragment)
                .commit();
    }
}
