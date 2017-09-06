package com.zhuoxin.treasurehunter.treasurehunter.treasure.detail;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.zhuoxin.treasurehunter.treasurehunter.R;
import com.zhuoxin.treasurehunter.treasurehunter.custom.TreasureView;
import com.zhuoxin.treasurehunter.treasurehunter.map.MapFragment;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Treasure;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TreasureDetailActivity extends AppCompatActivity implements TreasureDetailView{

    private static final String TREASURE = "treasure";
    @BindView(R.id.iv_navigation)
    ImageView mIvNavigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.detail_treasure)
    TreasureView mDetailTreasure;
    @BindView(R.id.tv_detail_description)
    TextView mTvDetailDescription;
    private Treasure mTreasure;

    public static void open(Context context, Treasure treasure) {
        Intent intent = new Intent(context, TreasureDetailActivity.class);
        intent.putExtra(TREASURE, treasure);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mTreasure = (Treasure) intent.getSerializableExtra(TREASURE);
        //设置ToolBar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置箭头
            getSupportActionBar().setTitle(mTreasure.getTitle());//设置标题
        }

        initMap();

        mDetailTreasure.bindView(mTreasure);

        TreasureDetail treasureDetail = new TreasureDetail(mTreasure.getId());
        new TreasureDetailPresenter(this).getTreasureDetail(treasureDetail);

    }

    private void initMap() {
        //设置地图状态
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(-20f)//地图俯仰角度
                .rotate(0f)//地图旋转角度
                .zoom(19)//地图缩放级别 3~21，数值越低，地图越大
                .target(new LatLng(mTreasure.getLatitude(),mTreasure.getLongitude()))//设置地图中心点
                .build();
        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus)//设置地图初始化时的地图状态
                .scaleControlEnabled(false)//设置是否显示比例尺控件
                .zoomControlsEnabled(false)//设置是否显示缩放控件
                .zoomGesturesEnabled(false)//设置是否允许缩放手势
                .compassEnabled(false)//设置是否允许指南针
                .scrollGesturesEnabled(false)//设置是否允许拖拽手势
                .rotateGesturesEnabled(false)//设置是否允许旋转手势
                .overlookingGesturesEnabled(false);//设置是否允许俯视手势

        MapView mapView = new MapView(this,options);
        BaiduMap baiduMap = mapView.getMap();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(mTreasure.getLatitude(),mTreasure.getLongitude()));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded));
        baiduMap.addOverlay(markerOptions);
        mFrameLayout.addView(mapView);
    }


    @Override//箭头的返回按钮
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //开启步行导航或者骑行导航的点击事件
    @OnClick(R.id.iv_navigation)
    public void showPopupMenu (){
        PopupMenu mPopupMenu = new PopupMenu(this, mIvNavigation);//创建一个PopupMenu
        mPopupMenu.inflate(R.menu.menu_navigation);//引入弹窗的布局
        final LatLng startPoint = MapFragment.getCurrentLocation();//导航起点坐标
        final String startAddress = MapFragment.getCurrentAddress();//导航起点名称
        double latitude = mTreasure.getLatitude();//纬度
        double longitude = mTreasure.getLongitude();//经度
        final LatLng endPoint = new LatLng(latitude, longitude);//导航终点坐标
        final String endAddress = mTreasure.getLocation();//导航终点名称
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.walking_navi://开启步行导航
                        openWalkNavigation(startPoint,startAddress ,endPoint,endAddress);
                        break;
                    case R.id.biking_navi://开启骑行导航
                        openBikeNavigation(startPoint,startAddress ,endPoint,endAddress);
                        break;
                }
                return false;
            }
        });
        mPopupMenu.show();
    }
    //开启骑行导航
    private void openBikeNavigation(LatLng startPoint, String startAddress, LatLng endPoint, String endAddress) {
        NaviParaOption mNaviParaOption = new NaviParaOption()
                .startPoint(startPoint)//导航起点,百度经纬度坐标
                .endPoint(endPoint)//导航终点,百度经纬度坐标
                .startName(startAddress)//导航起点名称
                .endName(endAddress);//导航终点名称
        boolean mMapBikeNavi = BaiduMapNavigation.openBaiduMapBikeNavi(mNaviParaOption, TreasureDetailActivity.this);
        if (!mMapBikeNavi){
            new AlertDialog.Builder(TreasureDetailActivity.this)
                    .setTitle("步行导航")
                    .setMessage("系统检测到您未安装百度地图App或地图版本过低，是否下载最新的百度地图？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OpenClientUtil.getLatestBaiduMapApp(TreasureDetailActivity.this);
                        }
                    })
                    .create()
                    .show();
        }
    }

    //开启步行导航
    private void openWalkNavigation(LatLng startPoint, String startAddress, LatLng endPoint, String endAddress) {
        NaviParaOption mNaviParaOption = new NaviParaOption()
                .startPoint(startPoint)
                .endPoint(endPoint)
                .startName(startAddress)
                .endName(endAddress);
        boolean mWalkNavi = BaiduMapNavigation.openBaiduMapWalkNavi(mNaviParaOption, TreasureDetailActivity.this);
        if (!mWalkNavi){
            BaiduMapNavigation.openWebBaiduMapNavi(mNaviParaOption,TreasureDetailActivity.this);
        }
    }
    //--------------------------------实现自视图接口的方法--------------------------------------
    @Override
    public void showTreasureDdetail(List<TreasureDetailResult> treasureDetailResultList) {
        if (treasureDetailResultList!=null){
            if (treasureDetailResultList.size()>=1){
                TreasureDetailResult treasureDetailResult = treasureDetailResultList.get(0);
                mTvDetailDescription.setText(treasureDetailResult.description);
                return;
            }
            mTvDetailDescription.setText("没有描述");
        }
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
