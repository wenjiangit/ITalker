package com.example.commom.face;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;

import java.util.Collections;
import java.util.List;

/**
 * Created by douliu on 2017/9/1.
 */

public class Face {


    /**
     * 拉取所有表情
     *
     * @param context Context
     * @return 表情盘的列表
     */
    public static List<FaceTab> all(@NonNull Context context) {
        return Collections.emptyList();
    }


    public static void input(@NonNull Context context, Editable editable) {
    }


/*
  public static void input(@NonNull Context context, Editable editable) {
    }
*/




    public static class FaceTab {

        public List<Bean> faces;
        public String name;
        public Object preview;


    }

    public static class Bean {

    }

}
