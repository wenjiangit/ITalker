package com.example.commom.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.commom.R;
import com.example.commom.factory.model.Author;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 圆形头像控件
 * Created by wenjian on 2017/6/4.
 */

public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager, String url) {
        this.setup(manager, R.drawable.default_portrait, url);
    }

    public void setup(RequestManager manager, Author author) {
        if (author == null) return;
        this.setup(manager, author.getPortrait());
    }


    public void setup(RequestManager manager, int holderId, String url) {
        if (url == null)//避免报错
            url = "";
        manager.load(url)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.placeholderOf(holderId))
                .apply(RequestOptions.noAnimation()) //不需要动画
                .into(this);
    }
}
