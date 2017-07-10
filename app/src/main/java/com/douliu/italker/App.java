package com.douliu.italker;

import android.app.Activity;
import android.os.Bundle;

import com.douliu.italker.services.MessageService;
import com.douliu.italker.services.PushService;
import com.example.commom.app.Application;
import com.example.factory.Factory;
import com.igexin.sdk.PushManager;

/**
 *
 * Created by wenjian on 2017/6/7.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Factory.setup();

        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this, PushService.class);

        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(),
                MessageService.class);

    }
}
