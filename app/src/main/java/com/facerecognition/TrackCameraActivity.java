package com.facerecognition;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import view.CameraView;

/**
 * Created by Seven on 2017/4/25.
 */

public class TrackCameraActivity extends AppCompatActivity {
    private CameraView cameraView;
    private static final String TAG = "TrackCameraActivity";
    private static final int REQUEST_CODE = 0;
    private static String[] permissions = {Manifest.permission.CAMERA};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facetract);
        init();
    }

    private void init() {
        cameraView = (CameraView) findViewById(R.id.trackCameraView);
        if (cameraView != null) {
            cameraView.loadOpenCV(this);
        }
        cameraView.switchCamera();
    }

    @Override
    protected void onResume() {
        MPermissions.requestPermissions(TrackCameraActivity.this, REQUEST_CODE, permissions);
        super.onResume();
    }

    //处理权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //授权成功
    @PermissionGrant(REQUEST_CODE)
    public void permissionSuccess() {
        Log.i(TAG, "permissionSuccess: 授权成功");
    }

    //授权失败
    @PermissionDenied(REQUEST_CODE)
    public void permissionFail() {
        Log.i(TAG, "permissionFail: 授权失败");
    }
}
