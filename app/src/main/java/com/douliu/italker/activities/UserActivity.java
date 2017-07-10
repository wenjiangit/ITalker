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
import com.douliu.italker.App;
import com.douliu.italker.R;
import com.douliu.italker.frags.user.UpdateInfoFragment;
import com.example.commom.app.BaseActivity;

import butterknife.BindView;

public class UserActivity extends BaseActivity {

    @BindView(R.id.im_bg)
    ImageView mImBg;

    private Fragment mFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mFragment = new UpdateInfoFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, mFragment)
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //将onActivityResult分发给mFragment
        mFragment.onActivityResult(requestCode, resultCode, data);
    }


    public static void show(Context context) {
        App.startActivity(context, UserActivity.class);
    }
}
