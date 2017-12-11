package com.douliu.italker.frags.message;


import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.douliu.italker.R;
import com.douliu.italker.activities.PersonalActivity;
import com.example.commom.widget.PortraitView;
import com.example.factory.model.db.User;
import com.example.factory.presenter.message.ChatContract;
import com.example.factory.presenter.message.UserChatPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 单聊界面
 * A simple {@link Fragment} subclass.
 * @author wenjian
 */
public class UserChatFragment extends ChatFragment<User>
        implements MenuItem.OnMenuItemClickListener,
        ChatContract.UserView {


    @BindView(R.id.im_portrait)
    PortraitView mImPortrait;
    private MenuItem mPersonMenuItem;


    public UserChatFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();

        mToolbar.inflateMenu(R.menu.user_chat);
        mPersonMenuItem = mToolbar.getMenu().findItem(R.id.action_person);

        mPersonMenuItem.setOnMenuItemClickListener(this);
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        //设置折叠布局遮罩
        Glide.with(this)
                .load(R.drawable.default_banner_chat)
                .apply(RequestOptions.centerCropTransform())
                .into(new ViewTarget<CollapsingToolbarLayout, Drawable>(mCollapsingToolbarLayout) {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        this.view.setContentScrim(resource);
                    }
                });
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        PersonalActivity.show(getActivity(), mReceiverId);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        Log.i(TAG, "verticalOffset: " + verticalOffset);

        if (verticalOffset == 0) {
            //完全展开
            mImPortrait.setScaleX(1);
            mImPortrait.setScaleY(1);
            mImPortrait.setImageAlpha(1);
            mImPortrait.setVisibility(View.VISIBLE);

            mPersonMenuItem.setVisible(false);
            mPersonMenuItem.getIcon().setAlpha(0);
        } else {
            verticalOffset = Math.abs(verticalOffset);
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {//完全收缩
                mImPortrait.setScaleX(0);
                mImPortrait.setScaleY(0);
                mImPortrait.setImageAlpha(0);
                mImPortrait.setVisibility(View.GONE);

                mPersonMenuItem.setVisible(true);
                mPersonMenuItem.getIcon().setAlpha(255);
            } else {
                float progress = 1 - verticalOffset / (float) totalScrollRange;
                mImPortrait.setScaleX(progress);
                mImPortrait.setScaleY(progress);
                mImPortrait.setImageAlpha((int) (progress * 255));
                mImPortrait.setVisibility(View.VISIBLE);

                mPersonMenuItem.getIcon().setAlpha((int) ((1 - progress) * 255));
                mPersonMenuItem.setVisible(true);
            }
        }
    }

    @Override
    public int getLayoutHeaderId() {
        return R.layout.lay_chat_user_header;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        PersonalActivity.show(getActivity(), mReceiverId);
        return true;
    }

    @Override
    protected ChatContract.Presenter createPresenter() {
        return new UserChatPresenter(this, mReceiverId);
    }

    @Override
    public void onInit(User user) {
        if (user == null) {
            return;
        }

        mCollapsingToolbarLayout.setTitle(user.getName());
        mImPortrait.setup(Glide.with(this), user);

    }
}
