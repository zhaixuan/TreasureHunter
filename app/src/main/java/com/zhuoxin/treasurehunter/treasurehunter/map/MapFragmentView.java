package com.zhuoxin.treasurehunter.treasurehunter.map;

import com.zhuoxin.treasurehunter.treasurehunter.treasure.Treasure;

import java.util.List;

/**
 * Created by Dionysus on 2017/8/31.
 */

public interface MapFragmentView {
    //展示宝藏
    void showTreasure(List<Treasure> treasureList);
    //展示信息
    void  showMessage(String msg);
}
