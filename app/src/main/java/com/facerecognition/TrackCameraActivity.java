package com.facerecognition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import view.CameraView;

/**
 * Created by Seven on 2017/4/25.
 */

public class TrackCameraActivity extends AppCompatActivity {
    private CameraView cameraView;
    private static final String TAG = "TrackCameraActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facetract);
        init();
    }

    private void init() {
        cameraView = (CameraView) findViewById(R.id.trackCameraView);
        if (cameraView != null) {
            cameraView.loadOpenCV(this);
        }
        cameraView.switchCamera();
    }
}
