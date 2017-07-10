package com.douliu.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.douliu.italker.R;
import com.douliu.italker.frags.account.AccountTrigger;
import com.douliu.italker.frags.account.LoginFragment;
import com.douliu.italker.frags.account.RegisterFragment;
import com.example.commom.app.BaseActivity;

import butterknife.BindView;

public class AccountActivity extends BaseActivity implements AccountTrigger {

    @BindView(R.id.im_bg)
    ImageView mImBg;

    private Fragment mCurFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;


    /**
     * AccountActivity的入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();

        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .apply(RequestOptions.centerCropTransform())
                .into(new ViewTarget<ImageView,Drawable>(mImBg) {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        //设置过滤色
                        resource.setColorFilter(getResources().getColor(R.color.colorAccent),
                                PorterDuff.Mode.SCREEN);
                        this.view.setImageDrawable(resource);
                    }
                });


    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        } else {
            fragment = mLoginFragment;
        }
        mCurFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container, mCurFragment)
                .commit();
    }


}
