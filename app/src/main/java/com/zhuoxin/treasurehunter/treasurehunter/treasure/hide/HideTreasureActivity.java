package com.zhuoxin.treasurehunter.treasurehunter.treasure.hide;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.zhuoxin.treasurehunter.treasurehunter.R;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.TreasureRepo;
import com.zhuoxin.treasurehunter.treasurehunter.user.UserPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dionysus on 2017/9/4.
 */

public class HideTreasureActivity extends AppCompatActivity implements HideTreasureView{
    private static final String TREASURE_TITLE = "treasure_title";
    private static final String TREASURE_LOCATION = "treasure_location";
    private static final String TREASURE_ADDRESS = "treasure_address";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    private String mTitle;
    private LatLng mLocation;
    private String mAddress;
    private ProgressDialog mProgressDialog;

    public static void open (Context context,String title,LatLng location,String address){
        Intent mIntent = new Intent(context, HideTreasureActivity.class);
        mIntent.putExtra(TREASURE_TITLE,title);
        mIntent.putExtra(TREASURE_LOCATION,location);
        mIntent.putExtra(TREASURE_ADDRESS,address);
        context.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_treasure);
        ButterKnife.bind(this);

        Intent mIntent = getIntent();
        mTitle = mIntent.getStringExtra(TREASURE_TITLE);
        mLocation = mIntent.getParcelableExtra(TREASURE_LOCATION);
        mAddress = mIntent.getStringExtra(TREASURE_ADDRESS);
        setSupportActionBar(mToolbar);//设置ToolBar
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置箭头
            getSupportActionBar().setTitle(mTitle);//设置标题
        }
    }

    @Override//设置箭头点击结束当前界面
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //埋藏宝藏的点击事件
    @OnClick(R.id.hide_send)
    public void onViewClicked() {
        HideTreasure mHideTreasure = new HideTreasure();
        mHideTreasure.setTokenId(UserPrefs.getInstance().getTokenid());
        mHideTreasure.setTitle(mTitle);
        mHideTreasure.setLocation(mAddress);
        mHideTreasure.setDescription(mEtDescription.getText().toString().trim());
        mHideTreasure.setLatitude(mLocation.latitude);
        mHideTreasure.setLongitude(mLocation.longitude);
        mHideTreasure.setAltitude(0.0);
        new HideTreasurePresenter(this).hideTreasure(mHideTreasure);
        //清理缓存，重新加载数据
        TreasureRepo.getInstance().clear();
    }
    //--------------------------实现自视图接口的方法-------------------------
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this,"宝藏上传","玩命上传中");
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backHome() {
        finish();
    }
}
