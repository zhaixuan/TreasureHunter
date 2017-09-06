package com.zhuoxin.treasurehunter.treasurehunter.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.zhuoxin.treasurehunter.treasurehunter.R;
import com.zhuoxin.treasurehunter.treasurehunter.map.MapFragment;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Treasure;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Dionysus on 2017/9/1.
 */

public class TreasureView extends RelativeLayout {
    @BindView(R.id.tv_treasureTitle)
    TextView mTvTreasureTitle;
    @BindView(R.id.tv_distance)
    TextView mTvDistance;
    @BindView(R.id.tv_treasureLocation)
    TextView mTvTreasureLocation;
    private Unbinder mUnbinder;

    public TreasureView(Context context) {
        super(context);
        init();
    }

    public TreasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TreasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.view_treasure, this, true);
        mUnbinder = ButterKnife.bind(mView);
    }

    public void bindView(Treasure treasure){
        String mTitle = treasure.getTitle();//宝藏名称
        String mLocation = treasure.getLocation();//宝藏的位置
        //获取宝物的位置
        double mLatitude = treasure.getLatitude();
        double mLongitude = treasure.getLongitude();
        LatLng mTreasurePosition = new LatLng(mLatitude, mLongitude);
        //获取寻宝者当前的位置
        LatLng mCurrentLocation = MapFragment.getCurrentLocation();
        //获取寻宝者与宝藏之间的距离
        double mDistance = DistanceUtil.getDistance(mTreasurePosition, mCurrentLocation);
        DecimalFormat mDecimalFormat = new DecimalFormat("#0.00");
        String formatDistance = mDecimalFormat.format(mDistance / 1000) + "km";

        mTvTreasureTitle.setText(mTitle);//设置宝藏名称
        mTvDistance.setText(formatDistance);//设置当前与宝藏的距离
        mTvTreasureLocation.setText(mLocation);//设置宝藏的位置
    }


   /* @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mUnbinder.unbind();
    }*/
}
