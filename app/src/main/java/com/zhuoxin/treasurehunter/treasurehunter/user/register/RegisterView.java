package com.zhuoxin.treasurehunter.treasurehunter.user.register;

/**
 * Created by Dionysus on 2017/8/29.
 */

public interface RegisterView {
    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    void navigateToHome();
}
