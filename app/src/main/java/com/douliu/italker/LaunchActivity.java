package com.douliu.italker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Property;
import android.widget.LinearLayout;

import com.douliu.italker.activities.AccountActivity;
import com.douliu.italker.activities.MainActivity;
import com.douliu.italker.frags.assist.PermissionsFragment;
import com.example.commom.app.BaseActivity;
import com.example.factory.persistant.Account;

import butterknife.BindView;

public class LaunchActivity extends BaseActivity {

    private ColorDrawable mBgDrawable;

    @BindView(R.id.activity_launch)
    LinearLayout mBgLayout;
    private int mStartColor;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mStartColor = ContextCompat.getColor(this, R.color.colorAccent);

        ColorDrawable colorDrawable = new ColorDrawable(mStartColor);
        mBgLayout.setBackground(colorDrawable);
        mBgDrawable = colorDrawable;


    }

    @Override
    protected void initData() {
        super.initData();

        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        });

    }

    /**
     * 等待个推框架为我们生成clientId
     */
    private void waitPushReceiverId() {
        if (Account.isLogin()) {//是否已经登录过
            if (Account.isBind()) {//是否已经绑定过pushId
                skip();
                return;
            }
        } else {
            if (checkPushId()) {
                skip();
                return;
            }
        }
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushReceiverId();
            }
        }, 500);

    }


    /**
     * 检查PushId
     *
     * @return 是否已经有了pushId
     */
    private boolean checkPushId() {
        return !TextUtils.isEmpty(Account.getPushId());
    }

    /**
     * 开始动画
     * @param fraction 动画进度
     * @param callback 完成时的回调
     */
    @SuppressWarnings("unchecked")
    private void startAnim(float fraction, final Runnable callback) {
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = ContextCompat.getColor(this, R.color.white);
        int evaluate = (int) evaluator.evaluate(fraction, mStartColor, endColor);

        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this,mProperty,evaluator,evaluate);

        valueAnimator.setIntValues(mBgDrawable.getColor(), evaluate);
        valueAnimator.setDuration(1500);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                callback.run();
            }
        });
        valueAnimator.start();
    }

    /**
     *  提供对应属性的get and set
     */
    private final Property<LaunchActivity,Integer> mProperty = new Property<LaunchActivity, Integer>(Integer.class,
           "color") {
        @Override
        public Integer get(LaunchActivity object) {
            return object.mBgDrawable.getColor();
        }

        @Override
        public void set(LaunchActivity object, Integer value) {
            object.mBgDrawable.setColor(value);
        }
    };

    /**
     *  真正的跳转操作
     */
    private void reallySkip() {
        if (PermissionsFragment.hasAllPerm(this, getSupportFragmentManager())) {
            if (Account.isLogin()) {
                MainActivity.show(this);
            } else {
                AccountActivity.show(this);
            }
            finish();
        }
    }

    /**
     *  继续没完成的动画
     */
    private void skip() {
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });
    }
}
