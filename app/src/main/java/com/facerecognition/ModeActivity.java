package com.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Seven on 2017/4/22.
 */

public class ModeActivity extends AppCompatActivity {
    private Button extractMode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode);
        init();
    }

    private void init() {
        extractMode= (Button) findViewById(R.id.extractMode);
        extractMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ModeActivity.this,ExtractActivity.class);
                startActivity(intent);
            }
        });
    }
}
