package com.zhuoxin.treasurehunter.treasurehunter.treasure.detail;

import java.util.List;

/**
 * Created by MACHENIKE on 2017/9/1.
 */

public interface TreasureDetailView {
    void showTreasureDdetail(List<TreasureDetailResult> treasureDetailResultList);
    void showMessage(String msg);
}
