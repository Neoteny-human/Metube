package com.android.metube.features.select.loader;



import android.content.Context;
import com.android.metube.features.select.loader.ILoader;
import com.android.metube.utils.callback.SimpleCallback;

/**
 * author : J.Chou
 * e-mail : who_know_me@163.com
 * time   : 2018/10/04 1:50 PM
 * version: 1.0
 * description:
 */
public class VideoLoadManager {

    private ILoader mLoader;

    public void setLoader(ILoader loader) {
        this.mLoader = loader;
    }

    public void load(final Context context, final SimpleCallback listener) {
        mLoader.load(context, listener);
    }
}

