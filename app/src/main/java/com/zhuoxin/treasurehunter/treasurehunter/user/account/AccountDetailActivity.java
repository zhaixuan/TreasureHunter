package com.zhuoxin.treasurehunter.treasurehunter.user.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pkmmte.view.CircularImageView;
import com.zhuoxin.treasurehunter.treasurehunter.R;
import com.zhuoxin.treasurehunter.treasurehunter.commons.ActivityUtils;
import com.zhuoxin.treasurehunter.treasurehunter.custom.IconSelectPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dionysus on 2017/9/5.
 */

public class AccountDetailActivity extends AppCompatActivity {
    @BindView(R.id.account_toolbar)
    Toolbar mAccountToolbar;
    @BindView(R.id.iv_usericon)
    CircularImageView mIvUsericon;
    private ActivityUtils mActivityUtils;
    private IconSelectPopupWindow mIconSelectPopupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mAccountToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("个人信息");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //点击头像弹出PopupWindow
    @OnClick(R.id.iv_usericon)
    public void onViewClicked() {
        if (mIconSelectPopupWindow == null){
            mIconSelectPopupWindow = new IconSelectPopupWindow(this,mOnItemClickListener);
            mIconSelectPopupWindow.show();
        }
    }

    private IconSelectPopupWindow.OnItemClickListener mOnItemClickListener = new IconSelectPopupWindow.OnItemClickListener() {
        @Override
        public void toGallery() {
            mActivityUtils.showToast("相册");
        }

        @Override
        public void toCamera() {
            mActivityUtils.showToast("相机");
        }

        @Override
        public void Cancel() {
            mActivityUtils.showToast("取消");
        }
    };
}
