package com.android.metube;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.android.metube.utils.BaseUtils;

/**
 * Author：J.Chou
 * Date：  2016.09.27 10:44 AM
 * Email： who_know_me@163.com
 * Describe:
 */
public class ZApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        BaseUtils.init(this);
//    initFFmpegBinary(this);
    }

//  private void initFFmpegBinary(Context context) {
//    if (!FFmpeg.getInstance(context).isSupported()) {
//      Log.e("ZApplication","Android cup arch not supported!");
//    }
//  }
}
