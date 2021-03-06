package com.douliu.italker;

import com.douliu.italker.services.MessageService;
import com.douliu.italker.services.PushService;
import com.example.commom.app.Application;
import com.example.factory.Factory;
import com.igexin.sdk.PushManager;

/**
 * @author wenjian
 * @date on 2017/6/7.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Factory.setup();

        // 为第三方自定义推送服务
        PushManager.getInstance().initialize(this, PushService.class);

        // 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(),
                MessageService.class);



    }


}
