package com.zhuoxin.treasurehunter.treasurehunter.user.login;

/**
 * Created by MACHENIKE on 2017/8/29.
 */

public interface LoginView {

    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    void navigateToHome();
}
