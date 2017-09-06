package com.zhuoxin.treasurehunter.treasurehunter.user.account;

/**
 * Created by Dionysus on 2017/9/5.
 */

public interface AccountView {
    void showProgress();

    void HideProgress();

    void showMessage(String msg);

    void display();
}
