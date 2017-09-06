package com.zhuoxin.treasurehunter.treasurehunter.treasure.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoxin.treasurehunter.treasurehunter.custom.TreasureView;
import com.zhuoxin.treasurehunter.treasurehunter.treasure.Treasure;

import java.util.List;

/**
 * Created by Dionysus on 2017/9/5.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Treasure> mTreasureList;

    public MyAdapter(List<Treasure> treasureList) {
        mTreasureList = treasureList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(new TreasureView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Treasure mTreasure = mTreasureList.get(position);
        holder.mTreasureView.bindView(mTreasure);
        holder.mTreasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.itemClickListener(mTreasure);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTreasureList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TreasureView mTreasureView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTreasureView = (TreasureView) itemView;
        }
    }

    interface OnItemClickListener{
        void itemClickListener(Treasure treasure);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }
}
