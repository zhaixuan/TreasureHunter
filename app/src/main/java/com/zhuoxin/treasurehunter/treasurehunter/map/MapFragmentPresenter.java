package com.zhuoxin.treasurehunter.treasurehunter.map;

import com.zhuoxin.treasurehunter.treasurehunter.net.NetClient;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Area;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Treasure;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.TreasureRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dionysus on 2017/8/31.
 */

public class MapFragmentPresenter {
    private MapFragmentView mMapFragmentView;

    public MapFragmentPresenter(MapFragmentView mapFragmentView) {
        mMapFragmentView = mapFragmentView;
    }

    public void getTreasureByArea(final Area area){
        if (TreasureRepo.getInstance().isCached(area)){
            //展示宝藏
            mMapFragmentView.showTreasure(TreasureRepo.getInstance().getTreasure());
            return;
        }
        NetClient.getInstance().getNetAPI().getTreasure(area).enqueue(new Callback<List<Treasure>>() {
            @Override
            public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
                if (response.isSuccessful()){
                    List<Treasure> mTreasureList = response.body();
                    if (mTreasureList == null){
                        mMapFragmentView.showMessage("未知错误");
                        return;
                    }

                    //缓存区域和宝藏
                    TreasureRepo.getInstance().cache(area);
                    TreasureRepo.getInstance().addTreasure(mTreasureList);
                    //展示宝藏
                    mMapFragmentView.showTreasure(mTreasureList);
                }
            }

            @Override
            public void onFailure(Call<List<Treasure>> call, Throwable t) {
                mMapFragmentView.showMessage("失败失败！");
            }
        });
    }
}
