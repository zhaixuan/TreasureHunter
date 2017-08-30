package com.zhuoxin.treasurehunter.treasurehunter.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dionysus on 2017/8/29.
 */

public class NetClient {
    public static final String BASE_URL = "http://admin.syfeicuiedu.com";
    private final Retrofit mRetrofit;
    private static NetClient mNetClient;

    public NetClient() {
        HttpLoggingInterceptor mInterceptor = new HttpLoggingInterceptor();
        mInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .build();
        Gson mGson = new GsonBuilder()
                .setLenient()
                .create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();
    }

    public static synchronized NetClient getInstance() {
        if (mNetClient == null) {
            mNetClient = new NetClient();
        }
        return mNetClient;
    }

    public NetAPI getNetAPI() {
        return mRetrofit.create(NetAPI.class);
    }
}
