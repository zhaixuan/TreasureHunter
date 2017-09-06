package com.zhuoxin.treasurehunter.treasurehunter.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhuoxin.treasurehunter.treasurehunter.MainActivity;
import com.zhuoxin.treasurehunter.treasurehunter.R;
import com.zhuoxin.treasurehunter.treasurehunter.commons.ActivityUtils;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.list.TreasureListFrament;
import com.zhuoxin.treasurehunter.treasurehunter.user.UserPrefs;
import com.zhuoxin.treasurehunter.treasurehunter.user.account.AccountDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private MapFragment mMapFragment;
    private TreasureListFrament mTreasureListFrament;
    private FragmentManager mSupportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        setSupportActionBar(mToolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mIvUserIcon = (ImageView) mNavigation.getHeaderView(0).findViewById(R.id.iv_usericon);
        //将Navigation与Toolbar关联起来
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //同步状态
        actionBarDrawerToggle.syncState();

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        //设置监听
        mNavigation.setNavigationItemSelectedListener(this);
        mMapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mSupportFragmentManager = getSupportFragmentManager();
        mTreasureListFrament = new TreasureListFrament();

        //点击跳转到个人详情界面
        mIvUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AccountDetailActivity.class));
            }
        });
    }

    @Override//利用Picasso设置侧滑菜单的用户头像
    protected void onStart() {
        super.onStart();
        String photo = UserPrefs.getInstance().getPhoto();
        if (photo!=null){
            Picasso.with(this).load(photo).into(mIvUserIcon);
        }
    }

    @Override//设置侧滑菜单menu的点击事件
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_hide://设置埋藏宝藏
                mMapFragment.changeUiMode(MapFragment.TREASURE_MODE_BURY);
                break;
            case R.id.menu_my_list://设置列表
                mActivityUtils.showToast("我的列表");
                break;
            case R.id.menu_help://设置帮助
                mActivityUtils.showToast("帮助");
                break;
            case R.id.menu_logout://设置退出登录
                UserPrefs.getInstance().clearUser();//清空用户数据
                mActivityUtils.startActivity(MainActivity.class);//跳转到主界面
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);//关闭侧滑菜单
        return false;
    }

    @Override//在activity创建的时候执行一次
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override//invalidateOptionsMenu()回掉的时候执行
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem mItem = menu.getItem(0);
        if (mTreasureListFrament!=null&&mTreasureListFrament.isAdded()){
            mItem.setIcon(R.drawable.ic_map);
        }else {
            mItem.setIcon(R.drawable.ic_view_list);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle){
            if (mTreasureListFrament!=null&&mTreasureListFrament.isAdded()){
                mSupportFragmentManager.popBackStack();
                mSupportFragmentManager.beginTransaction().remove(mTreasureListFrament).commit();
            }else {
                mSupportFragmentManager.beginTransaction().replace(R.id.fragment_container,mTreasureListFrament)
                        .addToBackStack(null).commit();
            }
        }
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)){
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }
        if (mMapFragment.isNormalMode()){
            super.onBackPressed();
        }
        mMapFragment.changeUiMode(MapFragment.TREASURE_MODE_NORMAL);
    }
}
