package com.zhuoxin.treasurehunter.treasurehunter.user.account;


import com.zhuoxin.treasurehunter.treasurehunter.net.NetClient;
import com.zhuoxin.treasurehunter.treasurehunter.user.UserPrefs;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dionysus on 2017/9/5.
 */

public class AccountPresent {

    private AccountView mAccountView;

    public AccountPresent(AccountView accountView) {
        mAccountView = accountView;
    }

    public void upLoad(File file){
        //显示进度条
        mAccountView.showProgress();
        final RequestBody requestBody = RequestBody.create(null, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("头像的URL", "Icon.jpg", requestBody);
        NetClient.getInstance().getNetAPI().upLoad(part).enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
                //隐藏进度条
                mAccountView.HideProgress();
                if (response.isSuccessful()){
                    UploadResult uploadResult = response.body();
                    if (uploadResult==null){
                        //showMessage
                        mAccountView.showMessage("未知错误");
                        return;
                    }

                    if (uploadResult.getCount()!=1){
                        //showMessage
                        mAccountView.showMessage(uploadResult.getMsg());
                        return;
                    }
                    String url = uploadResult.getUrl();
                    String photo = url.substring(url.lastIndexOf("/") + 1);
                    updateIcon(photo,url);
                }
            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {
                mAccountView.HideProgress();
                mAccountView.showMessage("失败");
            }
        });
    }

    private void updateIcon(final String photo, final String url) {

        final Update update = new Update(UserPrefs.getInstance().getTokenid(), photo);
        NetClient.getInstance().getNetAPI().updateIcon(update).enqueue(new Callback<UpdateResult>() {
            @Override
            public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                if (response.isSuccessful()){
                    UpdateResult updateResult = response.body();
                    if (updateResult==null){
                        //未知错误：
                        mAccountView.showMessage("未知错误");
                        return;
                    }
                    if (updateResult.getCode()!=1){
                        //message
                        mAccountView.showMessage(updateResult.getMsg());
                        return;
                    }
                    //缓存头像
                    UserPrefs.getInstance().setPhoto(NetClient.BASE_URL+url);
                    //displayPhoto
                    mAccountView.display();
                }
            }

            @Override
            public void onFailure(Call<UpdateResult> call, Throwable t) {
                mAccountView.HideProgress();
                //Msg
                mAccountView.showMessage("失败！！");
            }
        });

    }
}
