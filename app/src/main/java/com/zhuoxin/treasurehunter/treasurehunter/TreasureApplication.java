package com.zhuoxin.treasurehunter.treasurehunter;

import android.app.Application;

import com.zhuoxin.treasurehunter.treasurehunter.user.UserPrefs;


/**
 * Created by Dionysus on 2017/8/29.
 */

public class TreasureApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //一般进行一些初始化操作
        UserPrefs.init(getApplicationContext());
    }
}
