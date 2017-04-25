package com.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Seven on 2017/4/21.
 */

public class TrackActivity extends AppCompatActivity {
    private Button startCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track);
        init();
    }

    private void init() {
        startCamera= (Button) findViewById(R.id.startCamera);
        startCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TrackActivity.this,TrackCameraActivity.class);
                startActivity(intent);
            }
        });
    }
}
