package com.facerecognition;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;

/**
 * Created by marsor on 2017/5/12.
 */

public class LiveDetectActivity extends AppCompatActivity {
    private static final String TAG = "LiveDetectActivity";
    private static final String[] permissions={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
    private static final int PREQUEST_CODE = 0;
    private Button detect;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MPermissions.requestPermissions(this, PREQUEST_CODE, permissions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livedetect);
        init();
    }

    private void init(){
        createPath();
        detect= (Button) findViewById(R.id.detect);
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LiveDetectActivity.this,LiveDetectCameraActivity.class);
                startActivity(intent);
            }
        });
    }

    public static void createPath(){
        String path= Environment.getExternalStorageDirectory()+"/FaceStorage/";
        File file=new File(path);
        if(!file.exists()){
            file.mkdir();
        }
    }

    //处理权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //授权成功
    @PermissionGrant(PREQUEST_CODE)
    public void permissionSuccess() {
        Log.i(TAG, "permissionSuccess: 授权成功");
    }

    //授权失败
    @PermissionDenied(PREQUEST_CODE)
    public void permissionFail() {
        Log.i(TAG, "permissionFail: 授权失败");
    }
}
