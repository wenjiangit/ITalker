package com.example.factory.net;

import android.text.TextUtils;

import com.example.commom.Common;
import com.example.factory.BuildConfig;
import com.example.factory.Factory;
import com.example.factory.net.rx.RxRemoteService;
import com.example.factory.persistant.Account;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求
 * <p>
 *
 * @author wenjian
 * @date 2017/6/14
 */

public class Network {

    private Retrofit retrofit;

    private Network() {
    }

    private static class InstanceHolder {
        private static final Network INSTANCE = new Network();
    }

    private static Network instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 获取retrofit的实例
     *
     * @return Retrofit
     */
    private static Retrofit getRetrofit() {
        if (instance().retrofit != null) {
            return instance().retrofit;
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            builder.addHeader("token", Account.getToken());
                        }
                        builder.addHeader("Content-Type", "application/json");
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                });

        if (BuildConfig.DEBUG) {
            //添加日志拦截器
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        instance().retrofit = new Retrofit.Builder()
                .baseUrl(Common.Constants.API_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return instance().retrofit;
    }


    public static RemoteService remote() {
        return getRetrofit().create(RemoteService.class);
    }


    public static RxRemoteService rxRemote() {
        return getRetrofit().create(RxRemoteService.class);
    }


}
