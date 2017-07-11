package com.douliu.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.douliu.italker.R;
import com.example.commom.app.ToolbarActivity;
import com.example.factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends ToolbarActivity {

    public static void show(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.btn_login_out)
    Button mBtnLoginOut;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle(R.string.title_activity_settings);


    }

    @OnClick(R.id.btn_login_out)
    public void onViewClicked() {
        Factory.logout();
        AccountActivity.show(this);
    }
}
