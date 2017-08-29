package com.zhuoxin.treasurehunter.treasurehunter.user.login;

import com.zhuoxin.treasurehunter.treasurehunter.net.NetClient;
import com.zhuoxin.treasurehunter.treasurehunter.user.User;
import com.zhuoxin.treasurehunter.treasurehunter.user.UserPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dionysus on 2017/8/29.
 */

public class LoginPresenter {
    private LoginView mLoginView;

    public LoginPresenter(LoginView loginView) {
        mLoginView = loginView;
    }

    public void login(User user){
        //显示进度条
        mLoginView.showProgress();
        NetClient.getInstance().getNetAPI().login(user).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                //隐藏进度条
                mLoginView.hideProgress();
                if (response.isSuccessful()){
                    LoginResult userResult = response.body();
                    if (userResult==null){
                        //弹吐司
                        mLoginView.showMessage("未知错误");
                        return;
                    }
                    if (userResult.getErrcode()==1){
                        //缓存头像和tokenid
                        UserPrefs.getInstance().setPhoto(NetClient.BASE_URL+userResult.getHeadpic());
                        UserPrefs.getInstance().setTokenid(userResult.getTokenid());
                        //跳转到HomeActivity
                        mLoginView.navigateToHome();
                        return;
                    }else {
                        //弹吐司
                        mLoginView.showMessage(userResult.getErrmsg());
                        return;
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                //隐藏进度条
                mLoginView.hideProgress();
                //弹吐司
                mLoginView.showMessage("登陆失败！");
            }
        });
    }
}
