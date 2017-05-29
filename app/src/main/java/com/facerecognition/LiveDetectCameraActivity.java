package com.facerecognition;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by marsor on 2017/5/12.
 */

public class LiveDetectCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback,Camera.PictureCallback,View.OnClickListener{
    private static final String TAG = "LiveDetectCameraActivit";
    private static final int PREQUEST_CODE = 0;
    private static final String[] permissions={
            Manifest.permission.CAMERA};
    private static final int FRONT_CAMERA=1;
    private Boolean isSurfaceCreated=false;
    private Boolean isTimerRunning=false;
    private Handler handler=new Handler();
    private int currentTime=5;
    private SurfaceView liveDetectCamera;
    private SurfaceHolder surfaceHolder;
    private TextView count;
    private Button startCount;
    private Camera camera;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MPermissions.requestPermissions(this, PREQUEST_CODE, permissions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_detect_camera);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPreview();
    }

    private void init(){
        liveDetectCamera= (SurfaceView) findViewById(R.id.liveDetectCamera);
        startCount= (Button) findViewById(R.id.startCount);
        startCount.setOnClickListener(this);
        count= (TextView) findViewById(R.id.count);
        surfaceHolder=liveDetectCamera.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isSurfaceCreated=true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isSurfaceCreated=false;
    }

    private void startPreview(){
        if(camera!=null||!isSurfaceCreated){
            return;
        }
        camera=camera.open(FRONT_CAMERA);
        Camera.Parameters parameters=camera.getParameters();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        Camera.Size size = getBestPreviewSize(width, height, parameters);
        if (size != null) {
            //设置预览分辨率
            parameters.setPreviewSize(size.width, size.height);
            //设置保存图片的大小
            parameters.setPictureSize(size.width, size.height);
        }

        //自动对焦
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setPreviewFrameRate(20);

        //设置相机预览方向，模拟器上需注释掉
        camera.setDisplayOrientation(90);

        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    private void stopPreview(){
        if(camera!=null){
            try{
                camera.setPreviewDisplay(null);
                camera.stopPreview();
                camera.release();
                camera=null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Camera.Size getBestPreviewSize(int width, int height,
                                           Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return result;
    }

//    private Runnable timerRunnable=new Runnable() {
//        @Override
//        public void run() {
//            if (currentTime > 0) {
//                count.setText(currentTime + "");
//                currentTime--;
//                handler.postDelayed(timerRunnable, 1000);
//
//            } else {
//                count.setText("");
//                camera.takePicture(null, null, null, LiveDetectCameraActivity.this);
//                playSound();
//                isTimerRunning = false;
//                currentTime = 5;
//            }
//        }
//    };


    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        try {
            FileOutputStream fos = new FileOutputStream(new File
                    (Environment.getExternalStorageDirectory() +"/FaceStorage/"+ File.separator +
                            System.currentTimeMillis() + ".png"));

            //旋转角度，保证保存的图片方向是对的
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix matrix = new Matrix();
            //matrix.setRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.startPreview();
    }

    public void playSound() {
        MediaPlayer mediaPlayer = null;
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

        if (volume != 0) {
            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer.create(this,
                        Uri.parse("file:///system/media/audio/ui/camera_click.ogg"));
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startCount:{
//                if(!isTimerRunning){
//                    isTimerRunning=true;
//                    handler.post(timerRunnable);
//                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int j=0;j<5;j++) {
                            for (int i = 0; i < 5; i++) {
                                try {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            count.setText(currentTime + "");
                                        }
                                    });
                                    Thread.sleep(1000);
                                    currentTime--;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    count.setText("");
                                    //会把模拟器卡死
//                                    camera.takePicture(null,null,null,LiveDetectCameraActivity.this);
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            currentTime=5;
                        }
                    }
                }).start();
                break;
            }
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
