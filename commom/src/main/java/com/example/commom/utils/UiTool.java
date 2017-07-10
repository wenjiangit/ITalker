package com.example.commom.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import net.qiujuer.genius.ui.Ui;

import java.lang.reflect.Field;

/**
 * Created by wenjian on 2017/6/7.
 */

public class UiTool {


    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeigh(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }


    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int result = 0;
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            result = resources.getDimensionPixelSize(identifier);
        }

        if (result == 0) {
            try {
                @SuppressLint("PrivateApi")
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object instance = clazz.newInstance();
                Field field = clazz.getDeclaredField("status_bar_height");
                int id = Integer.valueOf(field.get(instance).toString());
                result = resources.getDimensionPixelSize(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (result == 0) {
            result = (int) Ui.dipToPx(resources, 25);
        }

        return result;

    }
}
