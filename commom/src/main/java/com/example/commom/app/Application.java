package com.example.commom.app;


import android.content.Context;
import android.content.Intent;

import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;

/**
 * 自定义Application
 *
 * Created by wenjian on 2017/6/7.
 */

public class Application extends android.app.Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }


    /**
     * 获取Application实例
     *
     * @return this
     */

    public static Application getInstance() {
        return instance;
    }

    /**
     * 获取头像临时缓存文件
     *
     * @return 临时文件
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File getPortraitTempFile() {
        File cacheDir = instance.getCacheDir();
        File dir = new File(cacheDir.getAbsolutePath(), "Portrait");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }

        return new File(dir, SystemClock.uptimeMillis() + ".jpg");
    }


    /**
     * Toast提示
     *
     * @param msg 内容
     */
    public static void showToast(final CharSequence msg) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Toast提示
     *
     * @param resId 资源id
     */
    public static void showToast(@StringRes int resId) {
        showToast(instance.getString(resId));
    }


    /**
     * 启动Activity
     * @param context 上下文
     * @param clazz clazz对象
     */
    public static void startActivity(Context context, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz));
    }
}
