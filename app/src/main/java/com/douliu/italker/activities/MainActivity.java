package com.douliu.italker.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.douliu.italker.App;
import com.douliu.italker.R;
import com.douliu.italker.frags.mian.ActiveFragment;
import com.douliu.italker.frags.mian.ContactFragment;
import com.douliu.italker.frags.mian.GroupFragment;
import com.example.commom.app.BaseActivity;
import com.example.commom.helper.NavHelper;
import com.example.commom.widget.PortraitView;
import com.example.factory.Factory;
import com.example.factory.persistant.Account;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnNavMenuChangedListener<Integer> {

    @BindView(R.id.im_portrait)
    PortraitView mImPortrait;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.im_search)
    ImageView mImSearch;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.lay_container)
    FrameLayout mLayContainer;
    @BindView(R.id.btn_action)
    FloatActionButton mBtnAction;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private NavHelper<Integer> mNavHelper;

    public static void show(Context context) {
        App.startActivity(context, MainActivity.class);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (Account.isComplete()) {//用户信息是否完善
            return super.initArgs(bundle);
        }
        UserActivity.show(this);
        return false;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mNavigation.setOnNavigationItemSelectedListener(this);

        mNavHelper = new NavHelper<>(this, getSupportFragmentManager(), R.id.lay_container, this);
        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home));
        mNavHelper.add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group));
        mNavHelper.add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));

        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .apply(RequestOptions.centerCropTransform())
                .into(new ViewTarget<View, Drawable>(mAppbar) {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        this.view.setBackground(resource);
                    }
                });

        mImPortrait.setup(Glide.with(this),Account.getUser());
    }


    @Override
    protected void initData() {
        super.initData();

        //手动设置menu点击
        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home, 0);
    }

    @OnClick({R.id.im_search, R.id.btn_action, R.id.im_portrait})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_search:
                NavHelper.Tab<Integer> currentTab = mNavHelper.getCurrent();
                if (Objects.equals(currentTab.extra, R.string.action_group)) {
                    SearchActivity.show(this, SearchActivity.TYPE_GROUP);
                } else {
                    SearchActivity.show(this, SearchActivity.TYPE_USER);
                }
                break;
            case R.id.btn_action:
                AccountActivity.show(this);
                Factory.logout();
                break;
            case R.id.im_portrait:
                PersonalActivity.show(this,Account.getUserId());
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mNavHelper.performMenuClick(item.getItemId());
    }

    @Override
    public void onNavTabChange(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        mTvTitle.setText(newTab.extra);

        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 76);
        } else {
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                rotation = 360;
                mBtnAction.setImageResource(R.drawable.ic_group_add);
            } else {
                rotation = -360;
                mBtnAction.setImageResource(R.drawable.ic_contact_add);
            }
        }

        mBtnAction.animate()
                .translationY(transY)
                .rotation(rotation)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();


    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }
}
