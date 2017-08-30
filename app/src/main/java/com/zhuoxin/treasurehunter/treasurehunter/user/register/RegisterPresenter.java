package com.zhuoxin.treasurehunter.treasurehunter.user.register;

import com.zhuoxin.treasurehunter.treasurehunter.net.NetClient;
import com.zhuoxin.treasurehunter.treasurehunter.user.User;
import com.zhuoxin.treasurehunter.treasurehunter.user.UserPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dionysus on 2017/8/29.
 */

public class RegisterPresenter {
    private RegisterView mRegisterView;

    public RegisterPresenter(RegisterView registerView) {
        mRegisterView = registerView;
    }

    public void register(User user){
        //显示进度条
        mRegisterView.showProgress();
        NetClient.getInstance().getNetAPI().register(user).enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                //隐藏进度条
                mRegisterView.hideProgress();
                if (response.isSuccessful()){
                    RegisterResult userResult = response.body();
                    if (userResult==null){
                        //弹吐司
                        mRegisterView.showMessage("未知错误");
                        return;
                    }
                    if (userResult.getErrcode()==1){
                        //缓存tokenid

                        UserPrefs.getInstance().setTokenid(userResult.getTokenid());
                        //跳转到HomeActivity
                        mRegisterView.navigateToHome();
                        return;
                    }else {
                        //弹吐司
                        mRegisterView.showMessage(userResult.getErrmsg());
                        return;
                    }
                }

            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                //隐藏进度条
                mRegisterView.hideProgress();
                //弹吐司
                mRegisterView.showMessage("注册失败！");
            }
        });
    }
}
