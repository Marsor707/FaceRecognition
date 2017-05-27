package com.facerecognition;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import utils.ImageUtil;
import utils.RecyclerViewAdapter;
import youtu.Youtu;

/**
 * Created by marsor on 2017/5/10.
 */

public class SearchResultActivity extends AppCompatActivity {
    private static final int BITMAP_LENGTH=5;
    private static final String TAG = "SearchResultActivity";
    private LinearLayoutManager mLinearManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView searchResult;
    private Bitmap[] bitmaps=new Bitmap[BITMAP_LENGTH];

    private static final String APP_ID="10081316";
    private static final String SECRET_ID="AKIDlqLgq4VynWnwufxRYqDsQqLhr8GEqQel";
    private static final String SECRET_KEY="Ed5MXcWHrGoG3hAoS0NZcT9d0WRDRlt3";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);
        init();
    }

    private void init(){
        searchResult= (RecyclerView) findViewById(R.id.searchResult);
        mLinearManager=new LinearLayoutManager(this);

        //！第一次打开APP上传的图片不能返回URL 下面的对比会报错
        //！所以就拿http://face--recognition.oss-cn-shanghai.aliyuncs.com/sampleObject.jpg先进行对比

//        Intent intent=getIntent();
//        String resultUrl=intent.getStringExtra("resultUrl");
//        Log.d(TAG, "Public Url"+resultUrl);
        final ProgressDialog progressDialog=ProgressDialog.show(SearchResultActivity.this,"提示","正在加载中",false);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                for(int i=0;i<5;i++){
//                    bitmaps[i]=ImageUtil.getURLBitMap("https://marsor707.github.io/assets/img/logo.png");
//                }
                String resultUrl="http://face--recognition.oss-cn-shanghai.aliyuncs.com/sampleObject.jpg";
                Youtu faceYoutu=new Youtu(APP_ID,SECRET_ID,SECRET_KEY,Youtu.API_TENCENTYUN_END_POINT);
                try {
                    JSONObject response=faceYoutu.FaceIdentifyUrl(resultUrl,"Face-Recognition");
                    Log.d(TAG, "Json "+response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                bitmaps[0]= ImageUtil.getURLBitMap("http://face--recognition.oss-cn-shanghai.aliyuncs.com/dcy1.jpg");
                bitmaps[1]= ImageUtil.getURLBitMap("http://face--recognition.oss-cn-shanghai.aliyuncs.com/dcy5.jpg");
                bitmaps[2]= ImageUtil.getURLBitMap("http://face--recognition.oss-cn-shanghai.aliyuncs.com/dcy3.jpg");
                bitmaps[3]= ImageUtil.getURLBitMap("http://face--recognition.oss-cn-shanghai.aliyuncs.com/dcy6.jpg");
                bitmaps[4]= ImageUtil.getURLBitMap("http://face--recognition.oss-cn-shanghai.aliyuncs.com/dcy2.jpg");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
        recyclerViewAdapter=new RecyclerViewAdapter(bitmaps);
        searchResult.setLayoutManager(mLinearManager);
        searchResult.setAdapter(recyclerViewAdapter);
    }
}
