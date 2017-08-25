package com.zhuoxin.treasurehunter.treasurehunter.custom;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoxin.treasurehunter.treasurehunter.commons.ActivityUtils;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Dionysus on 2017/8/25.
 */

public class MainMP4Fragment extends Fragment implements TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private ActivityUtils mActivityUtils;
    private MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTextureView = new TextureView(getContext());
        return mTextureView;
    }
    @Override//onCreateView执行完毕之后执行
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextureView.setSurfaceTextureListener(this);
        mActivityUtils = new ActivityUtils(this);
    }
    //---------------------------------------监听实现的方法-----------------------------------
    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        /*
        * TextureView准备好了，准备好了就可以播放视频了么？
        *
        * 需要读取视频资源
        * 需要一个视频播放控件MediaPlayer
        * */
        try {
            //读取视频资源
            AssetFileDescriptor mOpenFd = getContext().getAssets().openFd("welcome.mp4");
            //获取想要的文件类型
            FileDescriptor mDescriptor = mOpenFd.getFileDescriptor();
            mMediaPlayer = new MediaPlayer();
            //设置视频资源
            mMediaPlayer.setDataSource(mDescriptor,mOpenFd.getStartOffset(),mOpenFd.getLength());
            //异步准备
            mMediaPlayer.prepareAsync();
            //设置监听
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Surface mSurface = new Surface(surface);
                    mMediaPlayer.setSurface(mSurface);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            mActivityUtils.showToast("视频播放失败"+e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
    @Override//释放资源
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
