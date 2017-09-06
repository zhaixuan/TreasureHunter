package com.zhuoxin.treasurehunter.treasurehunter.treasure.hide;

import com.zhuoxin.treasurehunter.treasurehunter.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dionysus on 2017/9/4.
 */

public class HideTreasurePresenter {
    private HideTreasureView mHideTreasureView;

    public HideTreasurePresenter(HideTreasureView hideTreasureView) {
        mHideTreasureView = hideTreasureView;
    }

    public void hideTreasure(HideTreasure hideTreasure){
        mHideTreasureView.showProgress();
        NetClient.getInstance().getNetAPI().hideTreasure(hideTreasure).enqueue(new Callback<HideTreasureResult>() {
            @Override
            public void onResponse(Call<HideTreasureResult> call, Response<HideTreasureResult> response) {
                mHideTreasureView.hideProgress();
                if (response.isSuccessful()){
                    HideTreasureResult mHideTreasureResult = response.body();
                    if (mHideTreasureResult == null){
                        mHideTreasureView.showMessage("未知错误");
                        return;
                    }
                    if (mHideTreasureResult.getCode() != 1){
                        mHideTreasureView.showMessage(mHideTreasureResult.getMsg());
                        return;
                    }
                    //回到HomeActivity
                    mHideTreasureView.backHome();
                }
            }

            @Override
            public void onFailure(Call<HideTreasureResult> call, Throwable t) {
                mHideTreasureView.hideProgress();//隐藏进度条
                mHideTreasureView.showMessage("埋藏失败");//埋藏失败
            }
        });
    }
}
