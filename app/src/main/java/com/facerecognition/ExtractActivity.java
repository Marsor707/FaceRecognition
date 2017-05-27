package com.facerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import utils.ImageUtil;

/**
 * Created by Seven on 2017/4/22.
 */

public class ExtractActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG="ExtractActivity";
    private Intent intent;
    private ImageView faceModeImage;
    private Button saveModeImage;
    private Button keepModeImage;
    Bitmap ModeImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extract);
        init();
    }
    private void init(){
        intent=getIntent();
        faceModeImage= (ImageView) findViewById(R.id.faceModeImage);
        saveModeImage= (Button) findViewById(R.id.saveModeImage);
        keepModeImage= (Button) findViewById(R.id.keepModeImage);
        saveModeImage.setOnClickListener(this);
        keepModeImage.setOnClickListener(this);
        String Base64Data=intent.getStringExtra("base64");
        ModeImage= ImageUtil.base64ToBitmap(Base64Data);
        faceModeImage.setImageBitmap(ModeImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveModeImage:{
                ImageUtil.saveBitmap(this,ModeImage);
                break;
            }
            case R.id.keepModeImage:{
                finish();
                break;
            }
        }
    }
}
