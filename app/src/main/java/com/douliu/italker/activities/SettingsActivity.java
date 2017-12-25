package com.douliu.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.douliu.italker.App;
import com.douliu.italker.R;
import com.example.commom.app.ToolbarActivity;
import com.example.commom.widget.DataItemView;
import com.example.factory.Factory;

import butterknife.BindView;

/**
 * 设置界面
 *
 * @author wenjian
 */
public class SettingsActivity extends ToolbarActivity implements View.OnClickListener {

    public static void show(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.lay_container)
    LinearLayout mContainer;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle(R.string.title_activity_settings);

        LinearLayout.LayoutParams params = DataItemView.createLayoutParams(this, 20);

        DataItemView accountItem = new DataItemView.Builder(this)
                .topLine()
                .title("账号与安全")
                .next(this)
                .fullBottom()
                .build();
        mContainer.addView(accountItem, params);

        DataItemView messageItem = DataItemView.buider(this)
                .topLine()
                .title("新消息通知")
                .next(this)
                .build();
        mContainer.addView(messageItem, params);

        DataItemView secreteItem = DataItemView.buider(this)
                .title("隐私")
                .next(this)
                .build();
        mContainer.addView(secreteItem);

        DataItemView commonItem = DataItemView.buider(this)
                .title("通用")
                .next(this)
                .fullBottom()
                .build();
        mContainer.addView(commonItem);

        DataItemView helpItem = DataItemView.buider(this)
                .title("帮助与反馈")
                .next(this)
                .build();
        mContainer.addView(helpItem, params);

        DataItemView aboutItem = DataItemView.buider(this)
                .title("关于慕聊")
                .next(this)
                .fullBottom()
                .build();
        mContainer.addView(aboutItem);

        DataItemView logoutItem = DataItemView.buider(this)
                .title("退出登录")
                .buttonStyle()
                .next(R.id.setting_logout, this)
                .topLine()
                .fullBottom()
                .build();
        mContainer.addView(logoutItem, params);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_logout:
                Factory.logout();
                mContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.show(SettingsActivity.this);
                    }
                }, 500);
                break;
            default:
                if (v instanceof DataItemView) {
                    CharSequence title = ((DataItemView) v).getTitle();
                    App.showToast(title);
                }
                break;
        }
    }
}
