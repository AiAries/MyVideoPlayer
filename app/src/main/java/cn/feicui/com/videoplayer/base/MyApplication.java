package cn.feicui.com.videoplayer.base;

import android.app.Application;

import io.vov.vitamio.Vitamio;

/**
 * Created by Aries on 2016/10/23.
 */

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Vitamio.isInitialized(this);
    }
}
