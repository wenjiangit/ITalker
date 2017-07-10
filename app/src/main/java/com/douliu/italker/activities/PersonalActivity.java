package com.douliu.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.douliu.italker.R;
import com.example.commom.app.PresenterToolbarActivity;
import com.example.commom.widget.PortraitView;
import com.example.factory.model.db.User;
import com.example.factory.presenter.user.PersonalContract;
import com.example.factory.presenter.user.PersonalPresenter;

import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity extends PresenterToolbarActivity<PersonalContract.Presenter>
        implements PersonalContract.View {

    @BindView(R.id.txt_name)
    TextView mTxtName;
    @BindView(R.id.im_portrait)
    PortraitView mImPortrait;
    @BindView(R.id.txt_follows)
    TextView mTxtFollows;
    @BindView(R.id.txt_following)
    TextView mTxtFollowing;
    @BindView(R.id.txt_desc)
    TextView mTxtDesc;
    @BindView(R.id.btn_say_hello)
    Button mBtnSayHello;
    @BindView(R.id.im_header)
    ImageView mImHeader;

    private static final String EXTRA_USER_ID = "extra_user_id";
    private String userId;
    private MenuItem mFollowItem;
    private boolean isFollow;

    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        userId = bundle.getString(EXTRA_USER_ID);
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_follow) {
            followUser();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @OnClick(R.id.btn_say_hello)
    void sayHello() {
        User user = mPresenter.getPersonal();
        if (user != null) {
            MessageActivity.show(this, user);
        }
    }

    private void followUser() {
        // TODO: 2017/6/24 发起关注

    }

    /**
     * 刷新关注状态
     */
    private void refreshFollowState() {
        if (mFollowItem == null) {
            return;
        }
        Drawable drawable = isFollow ? ContextCompat.getDrawable(this, R.drawable.ic_favorite)
                : ContextCompat.getDrawable(this, R.drawable.ic_favorite_border);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Color.WHITE);
        mFollowItem.setIcon(drawable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal, menu);
        mFollowItem = menu.findItem(R.id.action_follow);
        refreshFollowState();
        return true;
    }

    @Override
    public void onLoadDone(User user) {
        hideLoading();
        if (user == null) {
            return;
        }
        mImPortrait.setup(Glide.with(this), user);
        mTxtDesc.setText(user.getDesc());
        mTxtName.setText(user.getName());
        mTxtFollowing.setText(String.format(getString(R.string.label_following), user.getFollowing()));
        mTxtFollows.setText(String.format(getString(R.string.label_follows), user.getFollows()));

       /* Glide.with(this)
                .load(user.getPortrait())
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.default_portrait))
                .apply(RequestOptions.noAnimation())
                .into(mImHeader);*/
    }

    @Override
    public void allowSayHello(boolean isAllow) {
        mBtnSayHello.setVisibility(isAllow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setFollowState(boolean isFollow) {
        this.isFollow = isFollow;
        refreshFollowState();
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    protected PersonalContract.Presenter createPresenter() {
        return new PersonalPresenter(this);
    }

}
