package com.zhuoxin.treasurehunter.treasurehunter.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.zhuoxin.treasurehunter.treasurehunter.R;
import com.zhuoxin.treasurehunter.treasurehunter.commons.ActivityUtils;
import com.zhuoxin.treasurehunter.treasurehunter.custom.TreasureView;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Area;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Treasure;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.TreasureRepo;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.detail.TreasureDetailActivity;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.hide.HideTreasureActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by MACHENIKE on 2017/8/30.
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
    @BindView(R.id.treasureView)
    TreasureView mTreasureView;
    @BindView(R.id.hide_treasure)
    RelativeLayout mHideTreasure;
    private BaiduMap mBaiduMap;
    private static String mCurrentAddress;
    private static LatLng mCurrentLocation;
    private LocationClient mLocationClient;
    private ActivityUtils mActivityUtils;
    private LatLng mCurrentStatus;
    private BitmapDescriptor mBitmapDescriptor;
    private BitmapDescriptor mDot;
    private Marker mCurrentmarker;
    private BitmapDescriptor mWindow;
    private InfoWindow mInfoWindow;
    private GeoCoder mGeoCoder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        //动态权限的添加
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mActivityUtils = new ActivityUtils(this);
        //初始化地图
        initMapView();
        //初始化位置
        initLocation();
        //地理编码
        initGeoCoder();
    }
    //地理编码
    private void initGeoCoder() {
        //设置地理编码查询
        mGeoCoder = GeoCoder.newInstance();
        //设置查询结果监听者
        mGeoCoder.setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener);
    }
    private String mCurrentGeoAddress;
    private OnGetGeoCoderResultListener mOnGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {
        @Override//正向地理编码
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override//反向地理编码
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null||reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR){
                mCurrentGeoAddress = "未知地址";
                mTvCurrentLocation.setText(mCurrentGeoAddress);
                return;
            }
            mCurrentGeoAddress = reverseGeoCodeResult.getAddress();
            mTvCurrentLocation.setText(mCurrentGeoAddress);
        }
    };

    public static final int TREASURE_MODE_NORMAL = 0;//普通模式
    public static final int TREASURE_MODE_SELECTED = 1;//选中模式
    public static final int TREASURE_MODE_BURY = 2;//埋藏宝藏

    public int treasureModeCurrent = TREASURE_MODE_NORMAL;
    public void changeUiMode(int mode){
        if (mode==treasureModeCurrent){
            return;
        }
        treasureModeCurrent =mode;
        switch (mode){
            case TREASURE_MODE_NORMAL:
                if (mCurrentmarker!=null){
                    mCurrentmarker.setVisible(true);
                }
                mCenterLayout.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();
                mLayoutBottom.setVisibility(View.GONE);
                mHideTreasure.setVisibility(View.GONE);
                break;
            case TREASURE_MODE_SELECTED:
                mCenterLayout.setVisibility(View.GONE);
                //mBaiduMap.showInfoWindow(mInfoWindow);
                //mLayoutBottom.setVisibility(View.VISIBLE);
                Log.e("=====","mLayoutBottom");
                mHideTreasure.setVisibility(View.GONE);
                mTreasureView.setVisibility(View.VISIBLE);
                break;
            case TREASURE_MODE_BURY:
                if (mCurrentmarker!=null){
                    mCurrentmarker.setVisible(true);
                }
                mCenterLayout.setVisibility(View.VISIBLE);
                mBaiduMap.hideInfoWindow();
                mLayoutBottom.setVisibility(View.GONE);
                mTreasureView.setVisibility(View.INVISIBLE);
                mBtnHideHere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTreasureView.setVisibility(View.INVISIBLE);
                        mLayoutBottom.setVisibility(View.VISIBLE);
                        mHideTreasure.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }

    @OnClick(R.id.treasureView)
    public void navigateToDetail(){
        int treasure_id = mCurrentmarker.getExtraInfo().getInt("treasure_id");
        Treasure treasure = TreasureRepo.getInstance().getTreasure(treasure_id);
        TreasureDetailActivity.open(getContext(),treasure);
    }
    //初始化位置
    private void initLocation() {
        //激活地图图层的定位功能
        mBaiduMap.setMyLocationEnabled(true);
        //第一步，初始化LocationClient类
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        //第二步，配置定位SDK参数
        LocationClientOption option = new LocationClientOption();
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        /*//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        mClientOption.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mClientOption.setIsNeedLocationDescribe(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mClientOption.setIgnoreKillProcess(false);*/

        mLocationClient.setLocOption(option);
        //第三步，注册监听，实现BDAbstractLocationListener接口
        mLocationClient.registerLocationListener(mBDLocationListener);
        //第四步，开始定位
        mLocationClient.start();

    }


    private BDLocationListener mBDLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double latitude = bdLocation.getLatitude(); //纬度
            double longitude = bdLocation.getLongitude();//经度
            //当前位置的经纬度
            mCurrentLocation = new LatLng(latitude, longitude);
            //获取获取详细地址信息
            mCurrentAddress = bdLocation.getAddrStr();
            Log.e("=========", "维度：" + latitude + " 经度：" + longitude + " 位置：" + mCurrentAddress);

            MyLocationData myLocationData = new MyLocationData.Builder()
                    .accuracy(100f)
                    .longitude(longitude)
                    .latitude(latitude)
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);
            moveToLocation();
        }
    };
    //百度地图的初始化
    private void initMapView() {
        //根据资源 Id 创建 bitmap 描述信息
        mDot = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_dot);
        mWindow = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0f)//地图俯仰角度
                .rotate(0f)//地图旋转角度
                .zoom(19f)//地图缩放级别 3~21，数值越低，地图越大
                .build();
        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus)//设置地图初始化时的地图状态
                .scaleControlEnabled(false)//设置是否显示比例尺控件
                .zoomControlsEnabled(false)//设置是否显示缩放控件
                .zoomGesturesEnabled(true);//设置是否允许缩放手势
        MapView mapView = new MapView(getContext(), options);
        mBaiduMap = mapView.getMap();
        mMapFrame.addView(mapView, 0);
        //地图状态改变监听
        mBaiduMap.setOnMapStatusChangeListener(mOnMapStatusChangeListener);
        //添加地图覆盖物的监听
        mBaiduMap.setOnMarkerClickListener(mOnMarkerClickListener);
    }

    //添加地图覆盖物的监听
    private BaiduMap.OnMarkerClickListener mOnMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override//一点击地图覆盖物就会触发该方法
        public boolean onMarkerClick(Marker marker) {
            if (mCurrentmarker != null) {//当前被点击的标记物不为空，说明，肯定有覆盖物被点击了
                mCurrentmarker.setVisible(true);
            }
            mCurrentmarker = marker;
            mCurrentmarker.setVisible(false);
            LatLng position = marker.getPosition();
            //mTreasureView.setVisibility(View.INVISIBLE);
            mInfoWindow = new InfoWindow(mWindow, position, 0, new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                    /*mBaiduMap.hideInfoWindow();
                    mCurrentmarker.setVisible(true);
                    mLayoutBottom.setVisibility(View.GONE);*/
                    //将模式的切换添加到弹窗的点击事件中
                    changeUiMode(TREASURE_MODE_NORMAL);
                }
            });
            int treasure_id = marker.getExtraInfo().getInt("treasure_id");
            Treasure treasure = TreasureRepo.getInstance().getTreasure(treasure_id);
            mTreasureView.bindView(treasure);
            mLayoutBottom.setVisibility(View.VISIBLE);
            /*//卡片布局的显示
            mLayoutBottom.setVisibility(View.VISIBLE);
            mTreasureView.setVisibility(View.VISIBLE);*/
            //展示宝藏的弹窗
            mBaiduMap.showInfoWindow(mInfoWindow);
            changeUiMode(TREASURE_MODE_SELECTED);
            return false;
        }
    };
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
            LatLng target = mapStatus.target;
            if (mCurrentStatus != target) {
                updateMapView(target);
                if (treasureModeCurrent == TREASURE_MODE_BURY){
                    ReverseGeoCodeOption mReverseGeoCodeOption = new ReverseGeoCodeOption()
                            .location(target);
                    mGeoCoder.reverseGeoCode(mReverseGeoCodeOption);
                }
            }
            mCurrentStatus = target;

        }
    };

    //获取宝藏更新地图
    private void updateMapView(LatLng target) {
        //网络请求获取宝藏
        double latitude = target.latitude;//纬度
        double longitude = target.longitude;//经度

        Area area = new Area();
        area.setMinLat(Math.floor(latitude));
        area.setMaxLat(Math.ceil(latitude));
        area.setMaxLng(Math.ceil(longitude));
        area.setMinLng(Math.floor(longitude));

        new MapFragmentPresenter(this).getTreasureByArea(area);
    }

    //定位的点击事件
    @OnClick({R.id.tv_located})
    public void moveToLocation() {
        //重新获取定位信息
        mLocationClient.requestLocation();
         /*方法一：
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(mCurrentLocation));*/
        //方法二：
        MapStatus mapStatus = new MapStatus.Builder()
                .target(mCurrentLocation)
                .zoom(19f)
                .overlook(0)
                .rotate(0)
                .build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));

    }
    //地图控件的点击事件
    @OnClick({R.id.tv_satellite, R.id.tv_compass, R.id.iv_scaleUp, R.id.iv_scaleDown})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_satellite://卫星，切换地图模式
                int mapType = mBaiduMap.getMapType();
                mapType = mapType == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
                String typeName = mapType == BaiduMap.MAP_TYPE_NORMAL ? "卫星" : "普通";
                mTvSatellite.setText(typeName);
                mBaiduMap.setMapType(mapType);
                break;
            case R.id.tv_compass://指南针
                boolean compassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
                mBaiduMap.getUiSettings().setCompassEnabled(!compassEnabled);
                break;
            case R.id.iv_scaleUp://放大
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.iv_scaleDown://缩小
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationClient.requestLocation();
                } else {
                    mActivityUtils.showToast("权限不足，请授权！");
                }
        }
    }
    //埋藏宝藏的点击事件
    @OnClick(R.id.hide_treasure)
    public void hideTreasure(){
        String mTitle = mEtTreasureTitle.getText().toString().trim();
        if (TextUtils.isEmpty(mTitle)){
            mActivityUtils.showToast("请输入宝藏的名称");
            return;
        }
        HideTreasureActivity.open(getContext(), mTitle,mCurrentStatus,mCurrentAddress);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //--------------------------实现自视图接口的方法-------------------------
    @Override
    public void showTreasure(List<Treasure> treasureList) {
        //清空上一次添加的地图覆盖物
        mBaiduMap.clear();
        if (treasureModeCurrent != TREASURE_MODE_BURY){
            changeUiMode(TREASURE_MODE_NORMAL);
        }
        //mLayoutBottom.setVisibility(View.GONE);
        //设置地图的覆盖物
        for (Treasure treasure : treasureList) {
            Bundle bundle = new Bundle();
            bundle.putInt("treasure_id", treasure.getId());
            MarkerOptions markerOptions = new MarkerOptions();
            //设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
            markerOptions.anchor(0.5f, 0.5f);
            //设置 marker 覆盖物的额外信息
            markerOptions.extraInfo(bundle);
            //设置 Marker 覆盖物的图标，相同图案的 icon 的 marker 最好使用同一个 BitmapDescriptor 对象以节省内存空间。
            markerOptions.icon(mDot);
            //设置 marker 覆盖物的位置坐标
            markerOptions.position(new LatLng(treasure.getLatitude(), treasure.getLongitude()));
            //向地图添加一个 Overlay
            mBaiduMap.addOverlay(markerOptions);
        }

    }

    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    public static LatLng getCurrentLocation() {
        return mCurrentLocation;
    }

    public static String getCurrentAddress () {
        return mCurrentAddress;
    }

    public boolean isNormalMode(){
        if (treasureModeCurrent == TREASURE_MODE_NORMAL){
            return true;
        }
        return false;
    }
}
