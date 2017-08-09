package com.example.factory.rx;

import com.example.factory.model.api.RspModel;

import io.reactivex.functions.Function;

/**
 *
 * Created by douliu on 2017/8/9.
 */

public class RxUtils {

    public static <R>Function<RspModel<R>, R> convert() {
        return new Function<RspModel<R>, R>() {
            @Override
            public R apply(RspModel<R> rRspModel) throws Exception {
                return rRspModel.getResult();
            }
        };
    }
}
