package com.example.commom.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity的基类
 * Created by douliu on 2017/6/2.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();

        if (initArgs(getIntent().getExtras())) {
            setContentView(getContentLayoutId());
            initBefore();
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    protected void initBefore() {
    }

    /**
     * 初始化窗口
     */
    protected void initWindows() {

    }

    /**
     * 初始化参数
     *
     * @param bundle 参数集合
     * @return 如果参数处理成功返回true, 否则返回false
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 获取布局资源id
     *
     * @return 资源id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {
        mUnbinder = ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        @SuppressLint("RestrictedApi")
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof BaseFragment) {
                    if (((BaseFragment) fragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
