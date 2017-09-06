package com.zhuoxin.treasurehunter.treasurehunter.net;


import com.zhuoxin.treasurehunter.treasurehunter.treasure.Area;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Treasure;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.detail.TreasureDetail;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.detail.TreasureDetailResult;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.hide.HideTreasure;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.hide.HideTreasureResult;
import com.zhuoxin.treasurehunter.treasurehunter.user.User;
import com.zhuoxin.treasurehunter.treasurehunter.user.account.Update;
import com.zhuoxin.treasurehunter.treasurehunter.user.account.UpdateResult;
import com.zhuoxin.treasurehunter.treasurehunter.user.account.UploadResult;
import com.zhuoxin.treasurehunter.treasurehunter.user.login.LoginResult;
import com.zhuoxin.treasurehunter.treasurehunter.user.register.RegisterResult;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    //根据区域获取帮藏
    @POST("/Handler/TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasure(@Body Area area);

    //获取宝藏详情
    @POST("/Handler/TreasureHandler.ashx?action=tdetails")
    Call<List<TreasureDetailResult>> getTreasureDetail(@Body TreasureDetail treasureDetail);

    //宝藏上传
    @POST("/Handler/TreasureHandler.ashx?action=hide")
    Call<HideTreasureResult> hideTreasure(@Body HideTreasure hideTreasure);

    //头像上传
    @Multipart
    @POST("/Handler/UserLoadPicHandler1.ashx")
    Call<UploadResult> upLoad(@Part MultipartBody.Part part);

    //头像跟新
    @POST("/Handler/UserHandler.ashx?action=update")
    Call<UpdateResult> updateIcon(@Body Update update);
}
