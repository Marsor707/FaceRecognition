package com.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by marsor on 2017/5/12.
 */

public class DetectActivity extends AppCompatActivity {
    private Button startDetect;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect);
        init();
    }

    private void init(){
        startDetect= (Button) findViewById(R.id.startDetect);
        startDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetectActivity.this,LiveDetectActivity.class);
                startActivity(intent);
            }
        });
    }

}
