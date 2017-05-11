package com.facerecognition;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.youth.banner.Banner;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.Arrays;

import utils.GlideImageLoader;
import utils.GridViewAdapter;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Banner banner;
    private GridView gridView;
    private GridViewAdapter adapter;
    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE = 0;
    private static final String[] permissions={
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
    private static Integer[] bannerImages={R.mipmap.banner1,R.mipmap.banner2,R.mipmap.banner3,R.mipmap.banner4};
    private static Integer[] gridImages={R.mipmap.track,R.mipmap.compare,R.mipmap.mode,R.mipmap.detect,R.mipmap.capture,R.mipmap.search};
    private static String[] gridTexts={"人像跟踪","人脸比对","人像建模","活体检测","人脸捕获","人脸搜索"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setCameraDisplayOrientation(MainActivity.this,0,switchCamera());
    }

    @Override
    protected void onResume() {
        MPermissions.requestPermissions(MainActivity.this, REQUEST_CODE, permissions);
        super.onResume();
    }

    private void init(){
        //图片轮播
        banner= (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(Arrays.asList(bannerImages));
        banner.setDelayTime(5000);
        banner.start();
        //gridView
        gridView= (GridView) findViewById(R.id.gridView);
        adapter=new GridViewAdapter(MainActivity.this,gridImages,gridTexts);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        //底部导航栏
//        bottomNavigationBar= (BottomNavigationBar) findViewById(R.id.bottomNavigation);
//        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.home,"Home"))
//                           .addItem(new BottomNavigationItem(R.mipmap.news,"News"))
//                           .addItem(new BottomNavigationItem(R.mipmap.about,"About Us"))
//                           .initialise();
//        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position){
            case 0:{
                Intent intent=new Intent(MainActivity.this,TrackActivity.class);
                startActivity(intent);
                break;
            }
            case 1:{
                Intent intent=new Intent(MainActivity.this,CompareActivity.class);
                startActivity(intent);
                break;
            }
            case 2:{
                Intent intent=new Intent(MainActivity.this,ModeActivity.class);
                startActivity(intent);
                break;
            }
            case 4:{
                Intent intent=new Intent(MainActivity.this,CaptureActivity.class);
                startActivity(intent);
                break;
            }
            case 5:{
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
            }
            default:break;
        }
    }

    private Camera switchCamera(){
        int numberOfCameras=0;
        numberOfCameras=Camera.getNumberOfCameras();
        if(numberOfCameras>1){
            Log.i(TAG, "switchCamera: 打开前置摄像头");
            return Camera.open(1);
        }
        Log.i(TAG, "switchCamera: 打开后置摄像头");
        return Camera.open();
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

    private static void setCameraDisplayOrientation (Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo (cameraId , info);
        int rotation = activity.getWindowManager ().getDefaultDisplay ().getRotation ();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;   // compensate the mirror
        } else {
            // back-facing
            result = ( info.orientation - degrees + 360) % 360;
        }
        Log.i(TAG, "setCameraDisplayOrientation: 调整了摄像头方向"+result);
        camera.setDisplayOrientation (result);
    }

/*
    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
*/
}
