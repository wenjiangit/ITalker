package com.example.commom.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.example.commom.R;

/**
 * 带Toolbar的Activity的基类
 *
 * Created by wenjian on 2017/6/21.
 */

public abstract class ToolbarActivity extends BaseActivity {

    protected Toolbar mToolbar;

    @Override
    protected void initWidget() {
        super.initWidget();
        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    public void initToolbar(Toolbar toolbar) {
        if (toolbar == null) {
            return;
        }
        mToolbar = toolbar;
        setSupportActionBar(mToolbar);
        initTitleNeedBack();
    }

    protected void initTitleNeedBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //设置返回按钮且可点击
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //设置返回按钮且可点击
            actionBar.setTitle(title);
        }
    }



}
