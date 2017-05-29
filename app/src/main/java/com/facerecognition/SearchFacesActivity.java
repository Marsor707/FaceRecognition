package com.facerecognition;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.FileNotFoundException;

import sample.PutObjectSamples;
import utils.ContentUriUtil;
import utils.ImageUtil;

/**
 * Created by marsor on 2017/5/9.
 */

public class SearchFacesActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final String TAG = "SearchFacesActivity";
    private static final String[] permissions={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PREQUEST_CODE = 0;
    private Bitmap myBitMap;
    private ImageButton addPhotoSearch;
    private Button search;

    private OSS oss;
    private static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    private static final String accessKeyId = "LTAILLuRRNwRNVjP";
    private static final String accessKeySecret = "RzN0stCV64kwYuTVRthwM7eGA2sAyi";
    private static String uploadFilePath = "";

    private static final String testBucket = "face--recognition";
    private static final String uploadObject = "sampleObject.jpg";
    private static final String downloadObject = "sampleObject";

    private static String finalUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MPermissions.requestPermissions(this, PREQUEST_CODE, permissions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facesearch);

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);

        init();
    }

    private void init() {
        addPhotoSearch = (ImageButton) findViewById(R.id.addPhotoSearch);
        search= (Button) findViewById(R.id.search);
        addPhotoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int ff = new PutObjectSamples(oss, testBucket, uploadObject, uploadFilePath).asyncPutObjectFromLocalFile();
                        // 但这里有个问题，为什么要摁两次upload从能打印出来Log.v中的url呢？？？
                        //if(ff == 200){
                            // 如果上传成功的话，那么返回对应的图片的url
                            finalUrl = "http://face--recognition.oss-cn-shanghai.aliyuncs.com/"+uploadObject;
                            Log.v("Final Url--->", finalUrl);
                        //}
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent(SearchFacesActivity.this,SearchResultActivity.class);
                                Log.i(TAG, "Public Url: "+finalUrl);
                                intent.putExtra("resultUrl",finalUrl);
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                uploadFilePath=ContentUriUtil.getPath(this,uri);
                Log.i(TAG, "onActivityResult: 绝对路径 "+uploadFilePath);
                Log.i(TAG, "onActivityResult: " + uri.toString());
                ContentResolver contentResolver = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    myBitMap = bitmap;
                    addPhotoSearch.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
            super.onActivityResult(requestCode, resultCode, data);
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