package com.android.metube.features.trim;


import static com.android.metube.features.trim.VideoTrimmerUtil.getVideoFilePath;
import static com.android.metube.features.trim.VideoTrimmerUtil.uploadWithTransferUtility;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.metube.R;
import com.android.metube.databinding.ActivityVideoTrimBinding;
import com.android.metube.features.common.ui.BaseActivity;
import com.android.metube.interfaces.VideoTrimListener;
import com.android.metube.utils.ToastUtil;

import java.io.File;

/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class VideoTrimmerActivity extends BaseActivity implements VideoTrimListener {

    private static final String TAG = "jason";
    private static final String VIDEO_PATH_KEY = "video-file-path";
    private static final String COMPRESSED_VIDEO_FILE_NAME = "compress.mp4";
    public static final int VIDEO_TRIM_REQUEST_CODE = 0x001;
    private ActivityVideoTrimBinding mBinding;
    private ProgressDialog mProgressDialog;

    public static void call(FragmentActivity from, String videoPath) {
        if (!TextUtils.isEmpty(videoPath)) {
            Bundle bundle = new Bundle();
            bundle.putString(VIDEO_PATH_KEY, videoPath);
            Intent intent = new Intent(from, VideoTrimmerActivity.class);
            intent.putExtras(bundle);
            from.startActivityForResult(intent, VIDEO_TRIM_REQUEST_CODE);
        }
    }

    @Override public void initUI() {

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_trim);
        Bundle bd = getIntent().getExtras();
        String path = "";
        if (bd != null) path = bd.getString(VIDEO_PATH_KEY);
        if (mBinding.trimmerView != null) {
            mBinding.trimmerView.setOnTrimVideoListener(this);
            mBinding.trimmerView.initVideoByURI(Uri.parse(path));
        }
    }

    @Override public void onResume() {
        super.onResume();
    }

    @Override public void onPause() {
        super.onPause();
        mBinding.trimmerView.onVideoPause();
        mBinding.trimmerView.setRestoreState(true);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mBinding.trimmerView.onDestroy();
    }

    @Override public void onStartTrim() {
        buildDialog(getResources().getString(R.string.trimming)).show();
    }

    @Override public void onFinishTrim(String outputName) {
//    if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
//    ToastUtil.longShow(this, getString(R.string.trimmed_done));
        String temp = "/storage/emulated/0/Movies/"+outputName;
        File file = new File(temp);
        Log.d(TAG, "onCompleted: 파일이름:"+file.getName());
        uploadWithTransferUtility(this, file.getName(), file);
        finish();
        //TODO: please handle your trimmed video url here!!!
        //여기서 편집한 동영상 url을 핸들링.

        //String out = StorageUtil.getCacheDir() + File.separator + COMPRESSED_VIDEO_FILE_NAME;
        //buildDialog(getResources().getString(R.string.compressing)).show();
        //VideoCompressor.compress(this, in, out, new VideoCompressListener() {
        //  @Override public void onSuccess(String message) {
        //  }
        //
        //  @Override public void onFailure(String message) {
        //  }
        //
        //  @Override public void onFinish() {
        //    if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
        //    finish();
        //  }
        //});

    }

    @Override public void onCancel() {
        mBinding.trimmerView.onDestroy();
//    String temp = "/storage/emulated/0/Movies/VID_20230125_012657.mp4";
//    File file = new File(temp);
//    Log.d(TAG, "onCompleted: 파일이름:"+file.getName()+file.getAbsolutePath());
//    uploadWithTransferUtility(getApplicationContext(), file.getName(), file);
        finish();
    }

    private ProgressDialog buildDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "", msg);
        }
        mProgressDialog.setMessage(msg);
        return mProgressDialog;
    }
}
