package com.zhuoxin.treasurehunter.treasurehunter.treasure.hide;

/**
 * Created by Dionysus on 2017/9/4.
 */

public interface HideTreasureView {
    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    void backHome();
}
