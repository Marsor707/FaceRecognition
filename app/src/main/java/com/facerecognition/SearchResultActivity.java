package com.facerecognition;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import utils.ImageUtil;
import utils.RecyclerViewAdapter;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);
        init();
    }

    private void init(){
        searchResult= (RecyclerView) findViewById(R.id.searchResult);
        mLinearManager=new LinearLayoutManager(this);
        final ProgressDialog progressDialog=ProgressDialog.show(SearchResultActivity.this,"提示","正在加载中",false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<5;i++){
                    bitmaps[i]=ImageUtil.getURLBitMap("https://marsor707.github.io/assets/img/logo.png");
                }
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
