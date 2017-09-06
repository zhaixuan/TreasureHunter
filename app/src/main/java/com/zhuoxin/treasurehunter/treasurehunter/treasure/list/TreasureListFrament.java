package com.zhuoxin.treasurehunter.treasurehunter.treasure.list;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuoxin.treasurehunter.treasurehunter.R;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Treasure;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.TreasureRepo;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.detail.TreasureDetailActivity;

/**
 * Created by Dionysus on 2017/9/5.
 */

public class TreasureListFrament extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mRecyclerView.setBackgroundResource(R.mipmap.screen_bg);
        MyAdapter mMyAdapter = new MyAdapter(TreasureRepo.getInstance().getTreasure());
        mRecyclerView.setAdapter(mMyAdapter);
        mMyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void itemClickListener(Treasure treasure) {
                TreasureDetailActivity.open(getContext(),treasure);
            }
        });
        return mRecyclerView;
    }
}
