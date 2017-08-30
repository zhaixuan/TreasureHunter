package com.zhuoxin.treasurehunter.treasurehunter.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhuoxin.treasurehunter.treasurehunter.MainActivity;
import com.zhuoxin.treasurehunter.treasurehunter.R;
import com.zhuoxin.treasurehunter.treasurehunter.commons.ActivityUtils;
import com.zhuoxin.treasurehunter.treasurehunter.user.UserPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Dionysus on 2017/8/29.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    private ActivityUtils mActivityUtils;
    private ImageView mIvUserIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mIvUserIcon = ((ImageView) mNavigation.getHeaderView(0).findViewById(R.id.iv_usericon));
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //同步状态
        mDrawerToggle.syncState();
        //设置监听
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigation.setNavigationItemSelectedListener(this);
    }

    @Override//利用Picasso设置侧滑菜单的用户头像
    protected void onStart() {
        super.onStart();
        String mPhoto = UserPrefs.getInstance().getPhoto();
        if (mPhoto != null){
            Picasso.with(this).load(mPhoto).into(mIvUserIcon);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_hide:
                // TODO: 2017/8/30
                mActivityUtils.showToast("埋藏宝藏");
                break;
            case R.id.menu_my_list:
                mActivityUtils.showToast("我的列表");
                break;
            case R.id.menu_help:
                mActivityUtils.showToast("帮助");
                break;
            case R.id.menu_logout:
                UserPrefs.getInstance().clearUser();
                mActivityUtils.startActivity(MainActivity.class);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
