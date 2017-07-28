package com.douliu.italker.frags.message;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.douliu.italker.R;
import com.douliu.italker.activities.GroupMemberActivity;
import com.douliu.italker.activities.PersonalActivity;
import com.example.factory.model.db.Group;
import com.example.factory.model.sample.MemberUserModel;
import com.example.factory.presenter.message.ChatContract;
import com.example.factory.presenter.message.GroupChatPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * 群聊界面
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends ChatFragment<Group>
        implements ChatContract.GroupView {

    @BindView(R.id.im_header)
    ImageView mImHeader;
    @BindView(R.id.txt_more)
    TextView mTxtMore;
    @BindView(R.id.lay_members)
    LinearLayout mLayMembers;

    public GroupChatFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        //设置折叠布局遮罩
        Glide.with(this)
                .load(R.drawable.default_banner_group)
                .apply(RequestOptions.centerCropTransform())
                .into(new ViewTarget<CollapsingToolbarLayout, Drawable>(mCollapsingToolbarLayout) {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        this.view.setContentScrim(resource);
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public int getLayoutHeaderId() {
        return R.layout.lay_chat_group_header;
    }

    @Override
    protected ChatContract.Presenter createPresenter() {
        return new GroupChatPresenter(this, mReceiverId);
    }

    @Override
    public void onInit(Group group) {
        mCollapsingToolbarLayout.setTitle(group.getName());
        //加载群头像做背景
        Glide.with(this)
                .load(group.getPicture())
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.default_banner_group))
                .into(mImHeader);
    }

    @Override
    public void showAdminOption(boolean isAdmin) {
        if (!isAdmin) {
            return;
        }
        mToolbar.inflateMenu(R.menu.group_chat);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_member_add) {
                // 添加群成员
                GroupMemberActivity.show(getContext(), mReceiverId);
                return true;
            }
            return false;
        });

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void showGroupMemberInfo(List<MemberUserModel> models, long moreCount) {
        if (models == null || models.size() == 0) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (MemberUserModel model : models) {
            ImageView portraitView = (ImageView) inflater.inflate(R.layout.chat_group_menber_portrait,
                    mLayMembers, false);

            mLayMembers.addView(portraitView, 0);

            Glide.with(this)
                    .load(model.portrait)
                    .apply(RequestOptions.placeholderOf(R.drawable.default_portrait))
                    .apply(RequestOptions.centerCropTransform())
                    .into(portraitView);

            //为头像添加点击事件
            portraitView.setOnClickListener(v -> PersonalActivity.show(getActivity(),model.userId));
        }

        if (moreCount > 0) {
            mTxtMore.setText(String.format("+%d", moreCount));
            mTxtMore.setOnClickListener(v -> {
                // 添加群成员
                GroupMemberActivity.show(getContext(), mReceiverId);
            });
        } else {
            mTxtMore.setVisibility(View.GONE);
        }

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        final View view = mLayMembers;
        if (verticalOffset == 0) {
            //完全展开
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);
            view.setVisibility(View.VISIBLE);

        } else {
            verticalOffset = Math.abs(verticalOffset);
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {//完全收缩
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);
                view.setVisibility(View.GONE);

            } else {
                float progress = 1 - verticalOffset / (float) totalScrollRange;
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress * 1);
                view.setVisibility(View.VISIBLE);
            }
        }
    }



}
