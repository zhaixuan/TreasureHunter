package com.zhuoxin.treasurehunter.treasurehunter.treasure.detail;

import com.zhuoxin.treasurehunter.treasurehunter.net.NetClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MACHENIKE on 2017/9/1.
 */

public class TreasureDetailPresenter {

    public TreasureDetailPresenter(TreasureDetailView treasureDetailView) {
        mTreasureDetailView = treasureDetailView;
    }

    private TreasureDetailView mTreasureDetailView;

    public void getTreasureDetail(TreasureDetail treasureDetail){
        NetClient.getInstance().getNetAPI().getTreasureDetail(treasureDetail).enqueue(new Callback<List<TreasureDetailResult>>() {
            @Override
            public void onResponse(Call<List<TreasureDetailResult>> call, Response<List<TreasureDetailResult>> response) {
                if (response.isSuccessful()){
                    List<TreasureDetailResult> treasureDetailResultList = response.body();
                    if (treasureDetailResultList==null){
                        //tusi
                        mTreasureDetailView.showMessage("未知错误");
                        return;
                    }
                    //showTreasureDetail(treasureDetailResultList)
                    mTreasureDetailView.showTreasureDdetail(treasureDetailResultList);
                }
            }

            @Override
            public void onFailure(Call<List<TreasureDetailResult>> call, Throwable t) {
                //tusi
                mTreasureDetailView.showMessage("失败");
            }
        });
    }
}
