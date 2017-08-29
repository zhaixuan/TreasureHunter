package com.zhuoxin.treasurehunter.treasurehunter.net;


import com.zhuoxin.treasurehunter.treasurehunter.user.User;
import com.zhuoxin.treasurehunter.treasurehunter.user.login.LoginResult;
import com.zhuoxin.treasurehunter.treasurehunter.user.register.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Dionysus on 2017/8/29.
 */

public interface NetAPI {
    //登录的post请求
    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User user);

    //注册的post请求
    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User user);
}