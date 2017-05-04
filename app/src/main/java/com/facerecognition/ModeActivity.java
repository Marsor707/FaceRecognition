package com.facerecognition;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.megvii.cloud.http.BodyOperate;
import com.megvii.cloud.http.Response;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Bitmap myBitMap;
    private Bitmap modeBitMap;
    private Button extractMode;
    private ImageButton addPhotoMode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode);
        init();
    }

    private void init() {
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


}
