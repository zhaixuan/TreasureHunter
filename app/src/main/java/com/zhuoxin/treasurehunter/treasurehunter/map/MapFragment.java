package com.zhuoxin.treasurehunter.treasurehunter.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.zhuoxin.treasurehunter.treasurehunter.R;
import com.zhuoxin.treasurehunter.treasurehunter.commons.ActivityUtils;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Area;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Treasure;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Dionysus on 2017/8/30.
 */

public class MapFragment extends Fragment implements MapFragmentView {
    private static final int REQUEST_CODE = 100;
    @BindView(R.id.iv_located)
    ImageView mIvLocated;
    @BindView(R.id.btn_HideHere)
    Button mBtnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout mCenterLayout;
    @BindView(R.id.iv_scaleUp)
    ImageView mIvScaleUp;
    @BindView(R.id.iv_scaleDown)
    ImageView mIvScaleDown;
    @BindView(R.id.tv_located)
    TextView mTvLocated;
    @BindView(R.id.tv_satellite)
    TextView mTvSatellite;
    @BindView(R.id.tv_compass)
    TextView mTvCompass;
    @BindView(R.id.ll_locationBar)
    LinearLayout mLlLocationBar;
    @BindView(R.id.tv_currentLocation)
    TextView mTvCurrentLocation;
    @BindView(R.id.iv_toTreasureInfo)
    ImageView mIvToTreasureInfo;
    @BindView(R.id.et_treasureTitle)
    EditText mEtTreasureTitle;
    @BindView(R.id.cardView)
    CardView mCardView;
    @BindView(R.id.layout_bottom)
    FrameLayout mLayoutBottom;
    @BindView(R.id.map_frame)
    FrameLayout mMapFrame;
    Unbinder unbinder;
    private BaiduMap mBaiduMap;
    private String mAddrStr;
    private LatLng mCurrentLocation;
    private LocationClient mLocationClient;
    private ActivityUtils mActivityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_map, null);
        //动态权限的添加
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        //初始化地图
        initMapView();
        //初始化位置
        initLocation();
    }
    //初始化位置
    private void initLocation() {
        //激活地图图层的定位功能
        mBaiduMap.setMyLocationEnabled(true);
        //第一步，初始化LocationClient类
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        //第二步，配置定位SDK参数
        LocationClientOption mClientOption = new LocationClientOption();
        mClientOption.setIsNeedAddress(true);
        mClientOption.setCoorType("bd09ll");
        mClientOption.setOpenGps(true);
        //mClientOption.setLocationNotify(true);
        //mClientOption.setIsNeedLocationDescribe(true);
        //mClientOption.setIgnoreKillProcess(false);
        mLocationClient.setLocOption(mClientOption);
        //第三步，注册监听，实现BDAbstractLocationListener接口
        mLocationClient.registerLocationListener(mBDLocationListener);
        //第四步，开始定位
        mLocationClient.start();
    }

    private BDLocationListener mBDLocationListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double mLatitude = bdLocation.getLatitude();//纬度
            double mLongitude = bdLocation.getLongitude();//经度
            mCurrentLocation = new LatLng(mLatitude,mLongitude);
            //获取获取详细地址信息
            mAddrStr = bdLocation.getAddrStr();
            Log.e("==========", "纬度：" + mLatitude + "经度："+ mLongitude + "位置：" + mAddrStr);

            MyLocationData mMyLocationData = new MyLocationData.Builder()
                    .accuracy(100f)
                    .latitude(mLatitude)
                    .longitude(mLongitude)
                    .build();
            mBaiduMap.setMyLocationData(mMyLocationData);
            moveToLocation();
        }
    };

    //百度地图的初始化
    private void initMapView() {
        MapStatus mMapStatus = new MapStatus.Builder()
                .overlook(0f)//地图俯仰角度
                .rotate(0f)//地图旋转角度
                .zoom(19)//地图缩放级别 3~21，数值越低，地图越大
                .build();
        BaiduMapOptions mBaiduMapOptions = new BaiduMapOptions()
                .mapStatus(mMapStatus)//设置地图初始化时的地图状态
                .scaleControlEnabled(false)//设置是否显示比例尺控件
                .zoomControlsEnabled(false)//设置是否显示缩放控件
                .zoomGesturesEnabled(true);//设置是否允许缩放手势
        MapView mMapView = new MapView(getContext(), mBaiduMapOptions);
        mBaiduMap = mMapView.getMap();
        mMapFrame.addView(mMapView, 0);
        //地图状态改变监听
        mBaiduMap.setOnMapStatusChangeListener(mOnMapStatusChangeListener);
    }
    //地图状态改变监听
    private BaiduMap.OnMapStatusChangeListener mOnMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            LatLng mTarget = mapStatus.target;
            if (mCurrentLocation != mTarget){
                updateMapView(mTarget);
            }
            mCurrentLocation = mTarget;
        }
    };

    //获取宝藏更新地图
    private void updateMapView(LatLng target){
        //网络请求获取宝藏
        double mLatitude = target.latitude;//纬度
        double mLongitude = target.longitude;//经度

        Area mArea = new Area();
        mArea.setMinLat(Math.floor(mLatitude));
        mArea.setMaxLat(Math.ceil(mLatitude));
        mArea.setMinLng(Math.floor(mLongitude));
        mArea.setMaxLng(Math.ceil(mLongitude));

        new MapFragmentPresenter(this).getTreasureByArea(mArea);
    }

    //定位的点击事件
    @OnClick({R.id.tv_located})
    public void moveToLocation(){
        //重新获取定位信息
        mLocationClient.requestLocation();
        /*方法一：
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(mCurrentLocation));*/
        //方法二：
        MapStatus mMapStatus = new MapStatus.Builder()
                .rotate(0f)
                .overlook(0f)
                .zoom(19)
                .target(mCurrentLocation)
                .build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
    }
    //地图控件的点击事件
    @OnClick({R.id.iv_scaleUp, R.id.iv_scaleDown, R.id.tv_satellite, R.id.tv_compass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scaleUp://放大
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.iv_scaleDown://缩小
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
            case R.id.tv_satellite://卫星，切换地图模式
                int mapType = mBaiduMap.getMapType();
                mapType = mBaiduMap.getMapType()==BaiduMap.MAP_TYPE_NORMAL?BaiduMap.MAP_TYPE_SATELLITE:BaiduMap.MAP_TYPE_NORMAL;
                String typeName = mapType==BaiduMap.MAP_TYPE_NORMAL?"卫星":"普通";
                mTvSatellite.setText(typeName);
                mBaiduMap.setMapType(mapType);
                break;
            case R.id.tv_compass://指南针
                boolean mCompassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
                mBaiduMap.getUiSettings().setCompassEnabled(!mCompassEnabled);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationClient.requestLocation();
                }else {
                    mActivityUtils.showToast("权限不足，请授权");
                }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //--------------------------实现自视图接口的方法-------------------------
    @Override
    public void showTreasure(List<Treasure> treasureList) {
        Log.e("=================",treasureList.size()+"");
    }

    @Override
    public void showMessage(String msg) {

    }
}
