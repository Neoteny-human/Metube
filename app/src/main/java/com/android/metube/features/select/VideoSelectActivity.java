package com.android.metube.features.select;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import com.android.metube.R;
import com.android.metube.databinding.ActivityVideoSelectBinding;
import com.android.metube.features.common.ui.BaseActivity;
//import com.android.metube.features.record.VideoRecordActivity;
import com.android.metube.features.record.view.PreviewSurfaceView;
import com.android.metube.features.select.loader.VideoCursorLoader;
import com.android.metube.features.select.loader.VideoLoadManager;
import com.android.metube.utils.callback.SimpleCallback;
import com.tbruyelle.rxpermissions3.RxPermissions;

/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class VideoSelectActivity extends BaseActivity implements View.OnClickListener {

    private ActivityVideoSelectBinding mBinding;
    private VideoSelectAdapter mVideoSelectAdapter;
    private VideoLoadManager mVideoLoadManager;
    private PreviewSurfaceView mSurfaceView;
    private ViewGroup mCameraSurfaceViewLy;

    @SuppressLint("CheckResult")
    @Override public void initUI() {
        mVideoLoadManager = new VideoLoadManager();
        mVideoLoadManager.setLoader(new VideoCursorLoader());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_select);
        mCameraSurfaceViewLy = findViewById(R.id.layout_surface_view);
        mBinding.mBtnBack.setOnClickListener(this);


        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
            if (granted) { // Always true pre-M
                mVideoLoadManager.load(this, new SimpleCallback() {
                    @Override public void success(Object obj) {
                        if (mVideoSelectAdapter == null) {
                            mVideoSelectAdapter = new VideoSelectAdapter(VideoSelectActivity.this, (Cursor) obj);
                        } else {
                            mVideoSelectAdapter.swapCursor((Cursor) obj);
                        }
                        if (mBinding.videoGridview.getAdapter() == null) {
                            mBinding.videoGridview.setAdapter(mVideoSelectAdapter);
                        }
                        mVideoSelectAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                finish();
            }
        });
//    if (rxPermissions.isGranted(Manifest.permission.CAMERA)) {
//      initCameraPreview();
//    } else {
//      mBinding.cameraPreviewLy.setVisibility(View.GONE);
//      mBinding.openCameraPermissionLy.setVisibility(View.VISIBLE);
//      mBinding.mOpenCameraPermission.setOnClickListener(new View.OnClickListener() {
//        @Override public void onClick(View v) {
//          rxPermissions.request(Manifest.permission.CAMERA).subscribe(granted -> {
//            if (granted) {
//              initCameraPreview();
//            }
//          });
//        }
//      });
//    }
    }

//  private void initCameraPreview() {
//    mSurfaceView = new PreviewSurfaceView(this);
//    mBinding.cameraPreviewLy.setVisibility(View.VISIBLE);
//    mBinding.openCameraPermissionLy.setVisibility(View.GONE);
//    addSurfaceView(mSurfaceView);
//    mSurfaceView.startPreview();
//
//    mBinding.cameraPreviewLy.setOnClickListener(v -> {
//      mSurfaceView.release();
//      VideoRecordActivity.call(this);
//    });
//  }

//  private void hideOtherView() {
//    mBinding.titleLayout.setVisibility(View.GONE);
//    mBinding.videoGridview.setVisibility(View.GONE);
//    mBinding.cameraPreviewLy.setVisibility(View.GONE);
//  }
//
//  private void resetHideOtherView() {
//    mBinding.titleLayout.setVisibility(View.VISIBLE);
//    mBinding.videoGridview.setVisibility(View.VISIBLE);
//    mBinding.cameraPreviewLy.setVisibility(View.VISIBLE);
//  }

    private void addSurfaceView(PreviewSurfaceView surfaceView) {
        mCameraSurfaceViewLy.addView(surfaceView);
    }

    @Override protected void onResume() {
        super.onResume();
        if (mSurfaceView != null) mSurfaceView.startPreview();
    }

    @Override protected void onPause() {
        super.onPause();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSurfaceView != null) mSurfaceView.startPreview();
    }

    @Override public void onClick(View v) {
        if (v.getId() == mBinding.mBtnBack.getId()) {
            finish();
        }
    }
}

