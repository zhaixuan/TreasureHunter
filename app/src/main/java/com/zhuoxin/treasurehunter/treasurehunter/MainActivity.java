package com.zhuoxin.treasurehunter.treasurehunter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhuoxin.treasurehunter.treasurehunter.commons.ActivityUtils;
import com.zhuoxin.treasurehunter.treasurehunter.user.login.LoginActivity;
import com.zhuoxin.treasurehunter.treasurehunter.user.register.RegisterActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ACTION = "main_action";
    private ActivityUtils mActivityUtils;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        IntentFilter mFilter = new IntentFilter(MAIN_ACTION);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mBroadcastReceiver,mFilter);
    }

    @OnClick({R.id.btn_Register, R.id.btn_Login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_Register://跳转至注册界面
                mActivityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.btn_Login://跳转至登录界面
                mActivityUtils.startActivity(LoginActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mBroadcastReceiver);
    }
}
