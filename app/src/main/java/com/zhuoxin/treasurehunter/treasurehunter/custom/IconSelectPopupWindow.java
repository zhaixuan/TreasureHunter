package com.zhuoxin.treasurehunter.treasurehunter.custom;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.zhuoxin.treasurehunter.treasurehunter.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dionysus on 2017/9/5.
 */

public class IconSelectPopupWindow extends PopupWindow {
    private final OnItemClickListener mOnItemClickListener;
    private Activity mActivity;

    public IconSelectPopupWindow(Activity activity, OnItemClickListener onItemClickListener) {
        super(activity.getLayoutInflater().inflate(R.layout.window_select_icon, null)
                , ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mActivity = activity;
        mOnItemClickListener = onItemClickListener;
        setBackgroundDrawable(new BitmapDrawable());
        ButterKnife.bind(this, getContentView());
    }

    public void show() {
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.btn_gallery, R.id.btn_camera, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_gallery://相册
                mOnItemClickListener.toGallery();
                break;
            case R.id.btn_camera://相机
                mOnItemClickListener.toCamera();
                break;
            case R.id.btn_cancel://取消
                mOnItemClickListener.Cancel();
                break;
        }
    }

    public static interface OnItemClickListener {
        void toGallery();

        void toCamera();

        void Cancel();
    }
}
