package com.example.factory.net;

import android.text.TextUtils;

import com.example.commom.Common;
import com.example.factory.Factory;
import com.example.factory.persistant.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求
 *
 * Created by wenjian on 2017/6/14.
 */

public class Network {

    private static Network instance;

    private Retrofit retrofit;

    private Network() {
    }

    static {
        instance = new Network();
    }

    /**
     * 获取retrofit的实例
     * @return Retrofit
     */
    private static Retrofit getRetrofit() {
        if (instance.retrofit != null) {
            return instance.retrofit;
        }

        OkHttpClient client = new OkHttpClient.Builder()
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
                }).build();

        instance.retrofit = new Retrofit.Builder()
                .baseUrl(Common.Constants.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

        return instance.retrofit;
    }


    public static RemoteService remote() {
        return getRetrofit().create(RemoteService.class);
    }




}
