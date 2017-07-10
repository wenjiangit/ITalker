package com.douliu.italker.services;

import android.content.Context;
import android.util.Log;

import com.example.factory.persistant.Account;
import com.example.factory.Factory;
import com.example.factory.data.helper.AccountHelper;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

/**
 * 个推消息接收服务
 *
 * Created by douliu on 2017/6/17.
 */

public class MessageService extends GTIntentService{

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.i(TAG, "onReceiveServicePid: " + pid);
    }

    @Override
    public void onReceiveClientId(Context context, String cid) {
        Log.i(TAG, "onReceiveClientId: " + cid);
        onClientInit(cid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        byte[] payload = gtTransmitMessage.getPayload();
        if (payload != null) {
            String message = new String(payload);
            Log.i(TAG, "onReceiveMessageData: " + message);
            onMessageArrived(message);
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        Log.i(TAG, "onReceiveOnlineState: "+b);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Log.i(TAG, "onReceiveCommandResult: " + gtCmdMessage);
    }

    /**
     * 保存clientId并绑定到服务器
     *
     * @param cid clientId
     */
    private void onClientInit(String cid) {
        Account.setPushId(cid);
        if (Account.isLogin()) {//如果登录了就进行绑定
            if (!Account.isBind()) {
                AccountHelper.bindPush(null);
            }
        }
    }

    /**
     * 交给Factory处理message
     *
     * @param message 收到的推送消息
     */
    private void onMessageArrived(String message) {
        Factory.dispatchMessage(message);
    }
}
