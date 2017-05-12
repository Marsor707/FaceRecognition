package com.facerecognition;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.megvii.cloud.http.BodyOperate;
import com.megvii.cloud.http.Response;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import utils.ImageUtil;

/**
 * Created by Seven on 2017/4/22.
 */

public class ModeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE1=1;
    private static final String TAG="ModeActivity";
    private static final String key = "YbgrHdNtC0p9OPtPHVCKdZJMJZgg6v7k";//api_key
    private static final String secret = "5d5CZ1Vp_k55cXNNpKQqdUwZFkl1vdfj";//api_secret
    private static final String[] permissions={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
    private static final int PREQUEST_CODE = 0;
    private Bitmap myBitMap;
    private Bitmap modeBitMap;
    private Button extractMode;
    private ImageButton addPhotoMode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MPermissions.requestPermissions(this, PREQUEST_CODE, permissions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode);
        init();
    }

    private void init() {
        createPath();
        extractMode= (Button) findViewById(R.id.extractMode);
        addPhotoMode= (ImageButton) findViewById(R.id.addPhotoMode);
        addPhotoMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE1);
            }
        });
        extractMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myBitMap != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BodyOperate bodyOperate = new BodyOperate(key, secret);
                            try {
                                Response response = bodyOperate.HumanBodySegment(null,null,ImageUtil.bitmapToBase64(myBitMap));
                                JSONObject json=getJSONObject(response);
                                final Intent intent=new Intent(ModeActivity.this,ExtractActivity.class);
                                intent.putExtra("base64",json.getString("result"));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(intent);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE1&&resultCode==RESULT_OK){
            if(data!=null){
                Uri uri = data.getData();
                Log.i(TAG, "onActivityResult: "+uri.toString());
                ContentResolver contentResolver = this.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    myBitMap=bitmap;
                    addPhotoMode.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private JSONObject getJSONObject(Response response) throws JSONException {
        String res=new String(response.getContent());
        JSONObject json=new JSONObject(res);
        return json;
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
