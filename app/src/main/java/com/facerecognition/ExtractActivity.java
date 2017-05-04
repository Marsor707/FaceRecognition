package com.facerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import utils.ImageUtil;

/**
 * Created by Seven on 2017/4/22.
 */

public class ExtractActivity extends AppCompatActivity {
    private static final String TAG="ExtractActivity";
    private Intent intent;
    private ImageView faceModeImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extract);
        init();
    }
    private void init(){
        intent=getIntent();
        faceModeImage= (ImageView) findViewById(R.id.faceModeImage);
        String Base64Data=intent.getStringExtra("base64");
        Bitmap ModeImage= ImageUtil.base64ToBitmap(Base64Data);
        faceModeImage.setImageBitmap(ModeImage);
    }
}
